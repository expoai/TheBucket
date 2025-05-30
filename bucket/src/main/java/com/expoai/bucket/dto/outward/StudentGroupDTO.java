package com.expoai.bucket.dto.outward;

import lombok.Builder;

@Builder
public record StudentGroupDTO(
        String username,
        String password,
        String apiKey
) {

}
