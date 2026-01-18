package com.expoai.bucket.dto.inward;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GenerationRequestSearchDTO(
        String promptKeyword,
        String descriptionKeyword,
        String status,
        Long userId,
        LocalDateTime createdAfter,
        LocalDateTime createdBefore
) {
}
