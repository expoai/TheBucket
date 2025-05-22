package com.expoai.bucket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record StudentUploadReadMetadataDTO(
    String idExterne,
    Long idTeam,
    String url,
    String tag1,
    String tag2,
    String tag3
    ){}
