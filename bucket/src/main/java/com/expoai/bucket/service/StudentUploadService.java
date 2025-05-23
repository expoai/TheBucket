package com.expoai.bucket.service;

import com.expoai.bucket.dto.*;
import com.expoai.bucket.entity.StudentUpload;
import com.expoai.bucket.entity.User;
import com.expoai.bucket.enums.MediaCategory;
import com.expoai.bucket.mapper.StudentUploadMapper;
import com.expoai.bucket.repository.StudentUploadRepository;
import com.expoai.bucket.repository.UserRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentUploadService {

    @Value("${minio.student-bucket:}")
    private String studentBucket;

    @Value("${minio.public-base-url}")
    private String publicBaseUrl;

    private final MinioClient minioClient;
    private final StudentUploadRepository studentUploadRepository;
    private final UserRepository userRepository ;
    private final StudentUploadMapper studentUploadMapper ;
    private final ThumbnailConverterService thumbnailConverterService ;

    public StudentUploadReadMetadataDTO save(UserDetails userdetails, StudentUploadWritingDTO upload) throws Exception {

        User user = userRepository.findByUsername(userdetails.getUsername()); ;
        MultipartFile file = upload.file() ;
        String contentType = file.getContentType();

        MediaCategory mediaCategory = MediaCategory
                .fromContentType(contentType)
                .orElseThrow(() -> new IllegalArgumentException("Unsupported file type: " + contentType));

        String sharedUrl = handleUpload(file, mediaCategory,false) ;
        StudentUpload.StudentUploadBuilder studentUploadBuilder = StudentUpload.builder() ;

        if(Boolean.TRUE.equals(upload.generateThumbnail())) {
            MultipartFile thumbnailImage = thumbnailConverterService.generateThumbnail(upload.file(), changeExtension(file.getOriginalFilename(), "webp")) ;
            String thumbnailSharedUrl = handleUpload(thumbnailImage, mediaCategory, true) ;
            studentUploadBuilder.thumbnailUrl(thumbnailSharedUrl) ;
        }

        studentUploadBuilder
                .idExterne(upload.idExterne())
                .tag1(upload.tag1())
                .tag2(upload.tag2())
                .tag3(upload.tag3())
                .team(user)
                .url(sharedUrl)
                .build();

        StudentUpload studentUpload = studentUploadRepository.save(studentUploadBuilder.build());
        return studentUploadMapper.uploadToReadingDTO(studentUpload) ;
    }

    public Optional<StudentUploadReadMetadataDTO> findByExternalId(UserDetails teamDetails, String externalID) {
        User team = userRepository.findByUsername(teamDetails.getUsername()); ;
        Optional<StudentUpload> uploadOpt = studentUploadRepository.findByTeamAndIdExterne(team, externalID);

        return uploadOpt
                .map(studentUploadMapper::uploadToReadingDTO);
    }

    public void delete(StudentUpload upload) throws Exception {
        String url = upload.getUrl() ;
        String baseUrl = publicBaseUrl + studentBucket ;
        String path = url.replace(baseUrl, "");
        int firstSlashIndex = path.indexOf('/');
        String objectKey = path.substring(firstSlashIndex + 1);

        minioClient.removeObject(
                RemoveObjectArgs
                        .builder()
                        .bucket(studentBucket).object(objectKey)
                        .build());

        studentUploadRepository.delete(upload) ;
    }

    public void delete(UserDetails teamDetails, String externalID) throws Exception {
        User team = userRepository.findByUsername(teamDetails.getUsername()); ;

        Optional<StudentUpload> uploadOpt = studentUploadRepository.findByTeamAndIdExterne(team, externalID);

        if(uploadOpt.isPresent()) {
            delete(uploadOpt.get());
        } else {
            throw new NoSuchElementException() ;
        }
    }

    @SneakyThrows
    public int purgeFiles(UserDetails teamDetails) throws Exception {
        User team = userRepository.findByUsername(teamDetails.getUsername()); ;

        List<StudentUpload> uploads = studentUploadRepository.findByTeam(team) ;

        for(StudentUpload upload : uploads) {
            try {
                delete(upload) ;
            } catch (Exception e) {
                System.out.println("Problem deleting " + e);
            }
        }

        return uploads.size() ;
    }

    public StudentUploadFindMetadataDTO findByTags(StudentPublicUploadFindDTO dto) {
        return StudentUploadFindMetadataDTO(dto.groupID(),dto.tag1(),dto.tag2(),dto.tag3()) ;
    }

    public StudentUploadFindMetadataDTO findByTags(UserDetails teamDetails, StudentUploadFindDTO dto) {
        User team = userRepository.findByUsername(teamDetails.getUsername());
        return StudentUploadFindMetadataDTO(team.getId(),dto.tag1(),dto.tag2(),dto.tag3()) ;
    }

    public StudentUploadFindMetadataDTO StudentUploadFindMetadataDTO(long teamID, String tag1,String tag2,String tag3) {
        List<StudentUpload> uploads = studentUploadRepository.findByTags(tag1, tag2, tag3, teamID) ;

        List<StudentUploadReadMetadataDTO> metadataDTOS = uploads
                .stream()
                .map(studentUploadMapper::uploadToReadingDTO)
                .toList() ;

        return new StudentUploadFindMetadataDTO(metadataDTOS) ;
    }

    public String handleUpload(MultipartFile file,MediaCategory mediaCategory, boolean forThumbnail) throws Exception {

        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String objectName =
                mediaCategory.getPrefix()
                + (forThumbnail ? "/thumbnail/": "/")
                + filename;

        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(studentBucket)
                    .object(objectName)
                    .stream(is, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        }

        String cleanBase = publicBaseUrl + studentBucket + "/";

        return cleanBase + objectName;
    }


    public static String changeExtension(String filename, String newExtension) {
        if (filename == null || newExtension == null) {
            throw new IllegalArgumentException("Filename and extension must not be null");
        }

        // Ensure the new extension starts with a dot
        if (!newExtension.startsWith(".")) {
            newExtension = "." + newExtension;
        }

        int dotIndex = filename.lastIndexOf('.');
        int sepIndex = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));

        // Dot must be after the last file separator to be considered an extension
        if (dotIndex > sepIndex) {
            return filename.substring(0, dotIndex) + newExtension;
        } else {
            return filename + newExtension;
        }
    }


}
/*
    public StudentUpload update(Long id, StudentUpload updated) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        return studentUploadRepository.findById(id).map(existing -> {
            if (!existing.getOwner().getUsername().equals(currentUser)) {
                throw new AccessDeniedException("You do not own this resource.");
            }

            existing.setIdExterne(updated.getIdExterne());
            existing.setUrl(updated.getUrl());
            existing.setTag1(updated.getTag1());
            existing.setTag2(updated.getTag2());
            existing.setTag3(updated.getTag3());

            return studentUploadRepository.save(existing);
        }).orElseThrow(() -> new NoSuchElementException("StudentUpload not found with ID " + id));
    }
*/