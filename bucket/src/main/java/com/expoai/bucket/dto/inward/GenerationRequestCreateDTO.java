package com.expoai.bucket.dto.inward;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record GenerationRequestCreateDTO(
        @NotBlank(message = "Prompt is required")
        @Size(min = 1, max = 500, message = "Prompt must be between 1 and 500 characters")
        String prompt,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        Long userId
) {
}
