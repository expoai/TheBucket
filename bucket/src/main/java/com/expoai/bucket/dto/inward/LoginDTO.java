package com.expoai.bucket.dto.inward;

import lombok.Builder;


@Builder
public record LoginDTO(String username, String password) {
}
