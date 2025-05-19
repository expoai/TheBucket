package com.expoai.bucket.service;

import com.expoai.bucket.dto.StudentUploadDTO;
import com.expoai.bucket.entity.StudentUpload;
import com.expoai.bucket.entity.UploadedImage;
import com.expoai.bucket.entity.User;
import com.expoai.bucket.enums.MediaCategory;
import com.expoai.bucket.enums.Visibility;
import com.expoai.bucket.repository.StudentUploadRepository;
import com.expoai.bucket.repository.UserRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
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

    public StudentUpload save(UserDetails userdetails, StudentUploadDTO upload) throws Exception {

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

        return studentUploadRepository.save(studentUpload);
    }

    /*
    public Optional<StudentUpload> findByExternalId(Long externalID) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        return studentUploadRepository.findById(externalID);
    }

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

    public void delete(Long id) throws AccessDeniedException {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        StudentUpload upload = studentUploadRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("StudentUpload not found with ID " + id));

        if (!upload.getOwner().getUsername().equals(currentUser)) {
            throw new AccessDeniedException("You do not own this resource.");
        }

        studentUploadRepository.deleteById(id);
    }
*/
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
