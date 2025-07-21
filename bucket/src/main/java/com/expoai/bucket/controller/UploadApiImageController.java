package com.expoai.bucket.controller;

import com.expoai.bucket.dto.outward.ApiSavedImagePublicDTO;
import com.expoai.bucket.enums.Visibility;
import com.expoai.bucket.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class UploadApiImageController {

    private final ImageUploadService imageUploadService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("visibility") Visibility visibility,
            @RequestParam("generateThumbnail") boolean generateThumbnail
    ) {
        try {
            ApiSavedImagePublicDTO publicUrl = imageUploadService.uploadImage(file, visibility, generateThumbnail);

            return ResponseEntity.ok(publicUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }


}
