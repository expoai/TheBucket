package com.expoai.bucket.controller;

import com.expoai.bucket.dto.StudentPublicUploadFindDTO;
import com.expoai.bucket.dto.StudentUploadFindMetadataDTO;
import com.expoai.bucket.service.StudentUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicAccessController {

    private final StudentUploadService studentUploadService;

    // TODO Public for student project reasons, might need to be disabled
    @GetMapping("/search")
    public ResponseEntity<?> findMetadata(@RequestBody StudentPublicUploadFindDTO dto) {
        if(dto.groupID() == 0) {
            return ResponseEntity.badRequest().body("groupID doit être renseigné") ;
        }

        return ResponseEntity.ok(studentUploadService.findByTags(dto));
    }

}
