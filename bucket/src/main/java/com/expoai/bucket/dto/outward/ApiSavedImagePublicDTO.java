package com.expoai.bucket.dto.outward;

import lombok.Builder;

@Builder
public record ApiSavedImagePublicDTO(
        String UUID,
        String publicURL,
        String thumbnailURL
) {
}
