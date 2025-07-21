package com.expoai.bucket.controller;

import com.expoai.bucket.dto.outward.ApiSavedImagePublicDTO;
import com.expoai.bucket.enums.Visibility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai_model")
@RequiredArgsConstructor
public class UploadApiAiModelController {

    // TODO this is for uploading models to be used later
    /*
    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam() MultipartFile file,
            @RequestParam(defaultValue = "true") Visibility visibility
    ) {
        try {
            ApiSavedImagePublicDTO publicUrl = imageUploadService.uploadImage(file, visibility, generateThumbnail);

            return ResponseEntity.ok(publicUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }
    */
}
