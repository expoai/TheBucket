package com.expoai.bucket.dto.outward;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GenerationRequestDTO(
        Long id,
        String prompt,
        String description,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long userId
) {
}
