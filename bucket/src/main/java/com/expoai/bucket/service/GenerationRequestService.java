package com.expoai.bucket.service;

import com.expoai.bucket.dto.inward.GenerationRequestCreateDTO;
import com.expoai.bucket.dto.inward.GenerationRequestSearchDTO;
import com.expoai.bucket.dto.outward.CursorPageDTO;
import com.expoai.bucket.dto.outward.GenerationRequestDTO;
import com.expoai.bucket.entity.GenerationRequest;
import com.expoai.bucket.mapper.GenerationRequestMapper;
import com.expoai.bucket.repository.GenerationRequestRepository;
import com.expoai.bucket.repository.GenerationRequestSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenerationRequestService {

    private final GenerationRequestRepository generationRequestRepository;
    private final GenerationRequestMapper generationRequestMapper;

    @Transactional
    public GenerationRequestDTO createRequest(GenerationRequestCreateDTO dto) {
        GenerationRequest request = generationRequestMapper.createDtoToEntity(dto);
        GenerationRequest saved = generationRequestRepository.save(request);
        return generationRequestMapper.entityToDto(saved);
    }

    @Transactional(readOnly = true)
    public Page<GenerationRequestDTO> getAllRequests(Pageable pageable) {
        return generationRequestRepository.findAll(pageable)
                .map(generationRequestMapper::entityToDto);
    }

    @Transactional(readOnly = true)
    public Optional<GenerationRequestDTO> getRequestById(Long id) {
        return generationRequestRepository.findById(id)
                .map(generationRequestMapper::entityToDto);
    }

    @Transactional
    public Optional<GenerationRequestDTO> updateStatus(Long id, String status) {
        return generationRequestRepository.findById(id)
                .map(request -> {
                    request.setStatus(status);
                    GenerationRequest updated = generationRequestRepository.save(request);
                    return generationRequestMapper.entityToDto(updated);
                });
    }

    @Transactional
    public boolean deleteRequest(Long id) {
        if (generationRequestRepository.existsById(id)) {
            generationRequestRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public CursorPageDTO<GenerationRequestDTO> getRequestsByCursor(Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size + 1);

        List<GenerationRequest> requests;
        if (cursor == null || cursor == 0) {
            requests = generationRequestRepository.findAllOrderByIdAsc(pageable);
        } else {
            requests = generationRequestRepository.findByCursorAsc(cursor, pageable);
        }

        boolean hasMore = requests.size() > size;
        if (hasMore) {
            requests = requests.subList(0, size);
        }

        Long nextCursor = null;
        if (hasMore && !requests.isEmpty()) {
            nextCursor = requests.get(requests.size() - 1).getId();
        }

        List<GenerationRequestDTO> dtos = requests.stream()
                .map(generationRequestMapper::entityToDto)
                .toList();

        return CursorPageDTO.<GenerationRequestDTO>builder()
                .data(dtos)
                .nextCursor(nextCursor)
                .hasMore(hasMore)
                .size(dtos.size())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<GenerationRequestDTO> searchRequests(GenerationRequestSearchDTO searchDTO, Pageable pageable) {
        Specification<GenerationRequest> specification = GenerationRequestSpecification.withSearchCriteria(searchDTO);
        return generationRequestRepository.findAll(specification, pageable)
                .map(generationRequestMapper::entityToDto);
    }
}
