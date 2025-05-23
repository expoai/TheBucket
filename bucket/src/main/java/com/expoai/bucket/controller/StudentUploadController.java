package com.expoai.bucket.controller;

import com.expoai.bucket.dto.StudentUploadFindDTO;
import com.expoai.bucket.dto.StudentUploadFindMetadataDTO;
import com.expoai.bucket.dto.StudentUploadReadMetadataDTO;
import com.expoai.bucket.dto.StudentUploadWritingDTO;
import com.expoai.bucket.service.StudentUploadService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/student/upload")
public class StudentUploadController {

    private final StudentUploadService studentUploadService;

    public StudentUploadController(StudentUploadService studentUploadService) {
        this.studentUploadService = studentUploadService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(
            @AuthenticationPrincipal UserDetails user,
            @ModelAttribute StudentUploadWritingDTO dto
    ) throws Exception {
        try {
            return ResponseEntity.ok(studentUploadService.save(user,dto));
        } catch (DataIntegrityViolationException e) {
                return ResponseEntity.badRequest()
                        .body("Erreur : un item avec ce même idExterne existe déjà pour votre équipe.");
        }

    }

    @GetMapping("/{externalID}")
    public ResponseEntity<StudentUploadReadMetadataDTO> getByExternalId(@AuthenticationPrincipal UserDetails user, @PathVariable String externalID) {
        return studentUploadService
                .findByExternalId(user, externalID)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserDetails user, @PathVariable String id) {
        try{
            studentUploadService.delete(user, id);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("Aucun élément avec l'ID : " + id + " pour votre équipe");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur avec le bucket, veillez me contacter");
        }

    }

    @GetMapping("/search")
    public ResponseEntity<StudentUploadFindMetadataDTO> searchByTags(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody StudentUploadFindDTO dto
    ) {
        return ResponseEntity.ok(studentUploadService.findByTags(user, dto));
    }

    @DeleteMapping("/purge")
    public ResponseEntity<?> purge(
            @AuthenticationPrincipal UserDetails user
    ) throws Exception {
        return ResponseEntity.ok(studentUploadService.purgeFiles(user) + " File(s) purged");
    }
}
