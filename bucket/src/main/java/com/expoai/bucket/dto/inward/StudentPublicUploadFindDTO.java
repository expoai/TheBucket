package com.expoai.bucket.dto.inward;

import lombok.Builder;

@Builder
public record StudentPublicUploadFindDTO(
        long groupID,
        String tag1,
        String tag2,
        String tag3
) {
}
