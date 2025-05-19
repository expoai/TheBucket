package com.expoai.bucket.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum MediaCategory {
    IMAGE("images", List.of("image/png", "image/jpeg", "image/webp")),
    AUDIO("audio", List.of("audio/mpeg", "audio/wav", "audio/x-wav")),
    JSON("json", List.of("application/json")),
    PDF("pdfs", List.of("application/pdf"))

    ;


    @Getter
    private final String prefix;
    private final List<String> allowedContentTypes;

    MediaCategory(String prefix, List<String> allowedContentTypes) {
        this.prefix = prefix;
        this.allowedContentTypes = allowedContentTypes;
    }

    public boolean isValidContentType(String contentType) {
        return allowedContentTypes.contains(contentType);
    }

    public static Optional<MediaCategory> fromContentType(String contentType) {
        return Arrays.stream(values())
                .filter(cat -> cat.isValidContentType(contentType))
                .findFirst();
    }
}
