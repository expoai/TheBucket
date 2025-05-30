package com.expoai.bucket.mapper;

import com.expoai.bucket.dto.outward.StudentUploadReadMetadataDTO;
import com.expoai.bucket.entity.StudentUpload;
import org.springframework.stereotype.Component;

@Component
public class StudentUploadMapper {

    public StudentUploadReadMetadataDTO uploadToReadingDTO(StudentUpload upload) {
        return StudentUploadReadMetadataDTO
                .builder()
                .idExterne(upload.getIdExterne())
                .url(upload.getUrl())
                .thumbnailUrl(upload.getThumbnailUrl())
                .tag1(upload.getTag1())
                .tag2(upload.getTag2())
                .tag3(upload.getTag3())
                .build() ;
    }


}