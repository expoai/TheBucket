package com.expoai.bucket.service;

import com.expoai.bucket.dto.outward.ApiSavedImagePublicDTO;
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

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final MinioClient minioClient;
    private final UploadedImageRepository uploadedImageRepository;
    //private final ThumbnailConverterService thumbnailConverterService;

    @Value("${minio.public-bucket}")
    private String publicBucket;

    @Value("${minio.private-bucket}")
    private String privateBucket;

    @Value("${minio.public-base-url}")
    private String publicBaseUrl;

    public ApiSavedImagePublicDTO uploadImage(MultipartFile file, String name, Visibility visibility, boolean generateThumbnail) throws Exception {
        String contentType = file.getContentType();

        MediaCategory mediaCategory = MediaCategory.fromContentType(contentType)
                .orElseThrow(() -> new IllegalArgumentException("Unsupported file type: " + contentType));


        UUID filePublicID = UUID.randomUUID() ;
        String objectName = mediaCategory.getPrefix() + "/" + filePublicID;

        String targetBucket = visibility == Visibility.PUBLIC ? publicBucket : privateBucket;
        String internalURL = handleUpload(file, targetBucket, objectName); ;

        UploadedImage image = new UploadedImage();
        image.setFilename(name != null ? name : file.getOriginalFilename());
        image.setCreatedAt(LocalDateTime.now());
        image.setVisibility(visibility);
        image.setPublicID(filePublicID);

        if (visibility == Visibility.PUBLIC) {
            image.setPublicUrl(publicBaseUrl + "/" + internalURL);
        }

        ApiSavedImagePublicDTO.ApiSavedImagePublicDTOBuilder savedImagePublicDTOBuilder = ApiSavedImagePublicDTO
                .builder()
                .UUID(filePublicID.toString())
                .publicURL(visibility == Visibility.PUBLIC
                        ? image.getPublicUrl()
                        : "/media/" + filePublicID)
                ;

        // DEPRECATED Does not work on prod server
        // GPU must have correct driver to manipulate image like this.
        /*
        if(generateThumbnail) {
            String objectThumbnailName = mediaCategory.getPrefix() + "/thumbnail/" + filePublicID;

            MultipartFile thumbnailImage = thumbnailConverterService.generateThumbnail(file) ;
            String thumbnailSharedUrl = handleUpload(thumbnailImage, targetBucket , objectThumbnailName);
            savedImagePublicDTOBuilder.thumbnailURL(publicBaseUrl + "/" + thumbnailSharedUrl) ;
        }
        */

        uploadedImageRepository.save(image);

        return savedImagePublicDTOBuilder.build() ;
    }

    public String handleUpload(MultipartFile file, String targetBucket, String fileName) throws Exception {
        String contentType = file.getContentType();

        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(targetBucket)
                    .object(fileName)
                    .stream(is, file.getSize(), -1)
                    .contentType(contentType)
                    .build());
        }


        return targetBucket + "/" + fileName;
    }

}