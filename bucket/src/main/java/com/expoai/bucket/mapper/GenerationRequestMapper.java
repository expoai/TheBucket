package com.expoai.bucket.mapper;

import com.expoai.bucket.dto.inward.GenerationRequestCreateDTO;
import com.expoai.bucket.dto.outward.GenerationRequestDTO;
import com.expoai.bucket.entity.GenerationRequest;
import org.springframework.stereotype.Component;

@Component
public class GenerationRequestMapper {

    public GenerationRequest createDtoToEntity(GenerationRequestCreateDTO dto) {
        return GenerationRequest.builder()
                .prompt(dto.prompt())
                .description(dto.description())
                .userId(dto.userId())
                .build();
    }

    public GenerationRequestDTO entityToDto(GenerationRequest entity) {
        return GenerationRequestDTO.builder()
                .id(entity.getId())
                .prompt(entity.getPrompt())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .userId(entity.getUserId())
                .build();
    }
}
