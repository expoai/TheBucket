package com.expoai.bucket.controller;

import com.expoai.bucket.annotation.LogExecutionTime;
import com.expoai.bucket.dto.inward.GenerationRequestCreateDTO;
import com.expoai.bucket.dto.inward.GenerationRequestSearchDTO;
import com.expoai.bucket.dto.outward.CursorPageDTO;
import com.expoai.bucket.dto.outward.GenerationRequestDTO;
import com.expoai.bucket.service.GenerationRequestService;
import com.expoai.bucket.service.NotificationService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/generation-request")
@RequiredArgsConstructor
public class GenerationRequestController {

    private final GenerationRequestService generationRequestService;

    private final NotificationService notificationService;

    @PostMapping
    @LogExecutionTime
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenerationRequestDTO> createRequest(@Valid @RequestBody GenerationRequestCreateDTO dto) {
        GenerationRequestDTO created = generationRequestService.createRequest(dto);
        notificationService.sendNotification("Notification done from Generation-request");
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @LogExecutionTime
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Page<GenerationRequestDTO>> getAllRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("ASC")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<GenerationRequestDTO> requests = generationRequestService.getAllRequests(pageable);

        return ResponseEntity.ok(requests);
    }

    @GetMapping("/cursor")
    @LogExecutionTime
    @RolesAllowed("ADMIN")
    public ResponseEntity<CursorPageDTO<GenerationRequestDTO>> getRequestsByCursor(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        CursorPageDTO<GenerationRequestDTO> result = generationRequestService.getRequestsByCursor(cursor, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @LogExecutionTime
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<GenerationRequestDTO> getRequestById(@PathVariable Long id) {
        return generationRequestService.getRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    @LogExecutionTime
    @PreAuthorize("hasAnyRole('ADMIN', 'API')")
    public ResponseEntity<GenerationRequestDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate
    ) {
        String status = statusUpdate.get("status");
        if (status == null || status.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        return generationRequestService.updateStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Example 7: Advanced multi-criteria search with partial matching
    @GetMapping("/search")
    @LogExecutionTime
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<GenerationRequestDTO>> searchRequests(
            @RequestParam(required = false) String promptKeyword,
            @RequestParam(required = false) String descriptionKeyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdBefore,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        GenerationRequestSearchDTO searchDTO = GenerationRequestSearchDTO.builder()
                .promptKeyword(promptKeyword)
                .descriptionKeyword(descriptionKeyword)
                .status(status)
                .userId(userId)
                .createdAfter(createdAfter)
                .createdBefore(createdBefore)
                .build();

        Sort.Direction sortDirection = direction.equalsIgnoreCase("ASC")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<GenerationRequestDTO> results = generationRequestService.searchRequests(searchDTO, pageable);

        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/{id}")
    @LogExecutionTime
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        boolean deleted = generationRequestService.deleteRequest(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
