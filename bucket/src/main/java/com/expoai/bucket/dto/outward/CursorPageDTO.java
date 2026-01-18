package com.expoai.bucket.dto.outward;

import lombok.Builder;

import java.util.List;

@Builder
public record CursorPageDTO<T>(
        List<T> data,
        Long nextCursor,
        boolean hasMore,
        int size
) {
}
