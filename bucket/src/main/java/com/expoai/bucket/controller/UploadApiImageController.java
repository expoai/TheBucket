package com.expoai.bucket.controller;

import com.expoai.bucket.annotation.LogExecutionTime;
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
    @LogExecutionTime
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam("visibility") Visibility visibility,
            @RequestParam(value = "generateThumbnail", defaultValue = "false") boolean generateThumbnail
    ) {
        try {

            if(generateThumbnail) {
                ResponseEntity
                        .badRequest()
                        .body("Generated Thumbnail is currently not supported");
            }

            ApiSavedImagePublicDTO publicUrl = imageUploadService.uploadImage(file, name, visibility, generateThumbnail);

            return ResponseEntity.ok(publicUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }


}
