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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
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

    public StudentUploadReadMetadataDTO save(UserDetails userdetails, StudentUploadWritingDTO upload) throws Exception {

        User user = userRepository.findByUsername(userdetails.getUsername()); ;
        String sharedUrl = handleUpload(upload.file()) ;

        StudentUpload studentUpload = StudentUpload
                .builder()
                .idExterne(upload.idExterne())
                .tag1(upload.tag1())
                .tag2(upload.tag2())
                .tag3(upload.tag3())
                .team(user)
                .url(sharedUrl)
                .build();

        studentUploadRepository.save(studentUpload);
        return studentUploadMapper.uploadToReadingDTO(studentUpload) ;
    }

    public Optional<StudentUploadReadMetadataDTO> findByExternalId(UserDetails teamDetails, String externalID) {
        User team = userRepository.findByUsername(teamDetails.getUsername()); ;
        Optional<StudentUpload> uploadOpt = studentUploadRepository.findByTeamAndIdExterne(team, externalID);

        return uploadOpt
                .map(studentUploadMapper::uploadToReadingDTO);
    }

    public void delete(UserDetails teamDetails, String externalID) throws Exception {
        User team = userRepository.findByUsername(teamDetails.getUsername()); ;

        Optional<StudentUpload> uploadOpt = studentUploadRepository.findByTeamAndIdExterne(team, externalID);
        if(uploadOpt.isPresent()) {
            StudentUpload upload = uploadOpt.get();
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

        } else {
            throw new NoSuchElementException() ;
        }
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

    public String handleUpload(MultipartFile file) throws Exception {
        String contentType = file.getContentType();

        MediaCategory mediaCategory = MediaCategory.fromContentType(contentType)
                .orElseThrow(() -> new IllegalArgumentException("Unsupported file type: " + contentType));

        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String objectName = mediaCategory.getPrefix() + "/" + filename;

        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(studentBucket)
                    .object(objectName)
                    .stream(is, file.getSize(), -1)
                    .contentType(contentType)
                    .build());
        }

        String cleanBase = publicBaseUrl + studentBucket + "/";

        return cleanBase + objectName;
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