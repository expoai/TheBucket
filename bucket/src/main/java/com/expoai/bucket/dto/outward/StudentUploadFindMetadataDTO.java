package com.expoai.bucket.dto.outward;

import lombok.Builder;

import java.util.List;

@Builder
public record StudentUploadFindMetadataDTO(
    List<StudentUploadReadMetadataDTO> studentUploadReadingDTOS){}
