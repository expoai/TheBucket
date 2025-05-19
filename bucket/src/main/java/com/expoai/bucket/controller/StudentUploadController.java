package com.expoai.bucket.controller;

import com.expoai.bucket.dto.StudentUploadDTO;
import com.expoai.bucket.entity.StudentUpload;
import com.expoai.bucket.service.StudentUploadService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentUploadController {

    private final StudentUploadService studentUploadService;

    public StudentUploadController(StudentUploadService studentUploadService) {
        this.studentUploadService = studentUploadService;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StudentUpload> create(
            @AuthenticationPrincipal UserDetails user,
            @ModelAttribute StudentUploadDTO dto
    ) throws Exception {

        return ResponseEntity.ok(studentUploadService.save(user,dto));
    }

    /*
    @GetMapping("/{id}")
    public ResponseEntity<StudentUpload> getByExternalId(@PathVariable Long externalID) {
        return studentUploadService
                .findByExternalId(externalID)
                .map(ResponseEntity.ok())
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentUpload> update(@PathVariable Long id, @RequestBody StudentUpload updated) {
        return ResponseEntity.ok(studentUploadService.update(id, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentUploadService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentUpload>> searchByTags(
            @RequestParam(required = false) String tag1,
            @RequestParam(required = false) String tag2,
            @RequestParam(required = false) String tag3
    ) {
        List<String> tags = Stream.of(tag1, tag2, tag3)
                .filter(tag -> tag != null && !tag.isBlank())
                .toList();

        return ResponseEntity.ok(studentUploadService.findByTags(idStudent, tags));
    }
    */
}
