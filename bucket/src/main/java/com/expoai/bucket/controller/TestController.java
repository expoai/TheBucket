package com.expoai.bucket.controller;

import com.expoai.bucket.service.ThumbnailConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    /*
    private final ThumbnailConverterService thumbnailConverterService;

    @GetMapping(path = "/thumbnail", produces = "image/webp")
    public ResponseEntity<byte[]> testPDF(MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("image/webp"));
        return ResponseEntity.ok().headers(headers).body(thumbnailConverterService.generateThumbnail(file));
    }
    */
}
