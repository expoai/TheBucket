package com.expoai.bucket.controller;

import com.expoai.bucket.dto.outward.ApiTokenAttributionDTO;
import com.expoai.bucket.enums.TokenType;
import com.expoai.bucket.service.ApiTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-token")
@RequiredArgsConstructor
public class ApiTokenController {

    private final ApiTokenService apiTokenService;

    @PostMapping
    public ResponseEntity<?> createToken(@AuthenticationPrincipal UserDetails user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        ApiTokenAttributionDTO apiAttributionDTO = ApiTokenAttributionDTO
                .builder()
                .token(apiTokenService.getApiToken(user.getUsername(), TokenType.API))
                .build()  ;

        return ResponseEntity.ok(apiAttributionDTO);
    }
}
