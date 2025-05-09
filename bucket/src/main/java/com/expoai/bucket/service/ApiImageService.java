package com.expoai.bucket.service;

import com.expoai.bucket.entity.UploadedImage;
import com.expoai.bucket.enums.MediaCategory;
import com.expoai.bucket.enums.Visibility;
import com.expoai.bucket.repository.UploadedImageRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiImageService {

    private final MinioClient minioClient;
    private final UploadedImageRepository uploadedImageRepository;

    @Value("${minio.public-bucket}")
    private String publicBucket;

    @Value("${minio.private-bucket}")
    private String privateBucket;

    @Value("${minio.public-base-url}") // e.g. https://yourdomain.com/public-media/
    private String publicBaseUrl;

    public String handleUpload(MultipartFile file, Visibility visibility) throws Exception {
        String contentType = file.getContentType();

        MediaCategory mediaCategory = MediaCategory.fromContentType(contentType)
                .orElseThrow(() -> new IllegalArgumentException("Unsupported file type: " + contentType));

        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String objectName = mediaCategory.getPrefix() + "/" + filename;

        String targetBucket = visibility == Visibility.PUBLIC ? publicBucket : privateBucket;

        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(targetBucket)
                    .object(objectName)
                    .stream(is, file.getSize(), -1)
                    .contentType(contentType)
                    .build());
        }

        UploadedImage image = new UploadedImage();
        image.setFilename(objectName);
        image.setCreatedAt(LocalDateTime.now());
        image.setVisibility(visibility);

        if (visibility == Visibility.PUBLIC) {
            String cleanBase = publicBaseUrl.endsWith("/") ? publicBaseUrl : publicBaseUrl + "/";
            image.setPublicUrl(cleanBase + objectName);
        }

        uploadedImageRepository.save(image);

        return visibility == Visibility.PUBLIC
                ? image.getPublicUrl()
                : "/media/" + filename;
    }

}
