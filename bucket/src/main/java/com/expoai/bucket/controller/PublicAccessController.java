package com.expoai.bucket.controller;

import com.expoai.bucket.dto.StudentPublicUploadFindDTO;
import com.expoai.bucket.dto.StudentUploadFindMetadataDTO;
import com.expoai.bucket.service.StudentUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/search/{groupID}")
    public ResponseEntity<?> findMetadata(
            @PathVariable long groupID,
            @RequestParam(required = false) String tag1,
            @RequestParam(required = false) String tag2,
            @RequestParam(required = false) String tag3
    ) {
        if(groupID == 0) {
            return ResponseEntity.badRequest().body("groupID doit être renseigné") ;
        }

        StudentPublicUploadFindDTO dto = StudentPublicUploadFindDTO
                .builder()
                .groupID(groupID)
                .tag1(tag1)
                .tag2(tag2)
                .tag3(tag3)
                .build() ;

        return ResponseEntity.ok(studentUploadService.findByTags(dto));
    }

}
