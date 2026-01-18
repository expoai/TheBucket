package com.expoai.bucket.repository;

import com.expoai.bucket.entity.GenerationRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenerationRequestRepository extends JpaRepository<GenerationRequest, Long>, JpaSpecificationExecutor<GenerationRequest> {

    @Query("SELECT gr FROM GenerationRequest gr WHERE gr.id > :cursor ORDER BY gr.id ASC")
    List<GenerationRequest> findByCursorAsc(@Param("cursor") Long cursor, Pageable pageable);

    @Query("SELECT gr FROM GenerationRequest gr ORDER BY gr.id ASC")
    List<GenerationRequest> findAllOrderByIdAsc(Pageable pageable);

}
