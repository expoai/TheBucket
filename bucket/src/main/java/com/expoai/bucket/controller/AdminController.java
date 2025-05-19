package com.expoai.bucket.controller;

import com.expoai.bucket.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/create-team")
    public ResponseEntity<?> createTeamAndToken(@RequestParam String name) {
        return ResponseEntity.ok(adminService.createStudentGroup(name));
    }

}
