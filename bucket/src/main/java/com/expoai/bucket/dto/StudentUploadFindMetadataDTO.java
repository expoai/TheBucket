package com.expoai.bucket.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record StudentUploadFindMetadataDTO(
    List<StudentUploadReadMetadataDTO> studentUploadReadingDTOS){}
