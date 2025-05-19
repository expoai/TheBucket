package com.expoai.bucket.controller;

import com.expoai.bucket.enums.Visibility;
import com.expoai.bucket.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiImageController {

    private final ImageUploadService imageUploadService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("visibility") Visibility visibility
    ) {
        try {
            String publicUrl = imageUploadService.handleUpload(file, visibility);
            return ResponseEntity.ok(publicUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }


}
