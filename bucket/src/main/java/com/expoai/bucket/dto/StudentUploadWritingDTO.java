package com.expoai.bucket.dto;

import org.springframework.web.multipart.MultipartFile;

public record StudentUploadWritingDTO(
        MultipartFile file,
        String idExterne,
        String tag1,
        String tag2,
        String tag3,
        Boolean  generateThumbnail
){
}
