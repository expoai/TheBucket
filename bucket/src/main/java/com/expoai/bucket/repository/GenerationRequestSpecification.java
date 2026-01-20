package com.expoai.bucket.repository;

import com.expoai.bucket.dto.inward.GenerationRequestSearchDTO;
import com.expoai.bucket.entity.GenerationRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class GenerationRequestSpecification {

    public static Specification<GenerationRequest> withSearchCriteria(GenerationRequestSearchDTO searchDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchDTO.promptKeyword() != null && !searchDTO.promptKeyword().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("prompt")),
                        "%" + searchDTO.promptKeyword().toLowerCase() + "%"
                ));
            }

            if (searchDTO.descriptionKeyword() != null && !searchDTO.descriptionKeyword().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        "%" + searchDTO.descriptionKeyword().toLowerCase() + "%"
                ));
            }

            if (searchDTO.status() != null && !searchDTO.status().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), searchDTO.status()));
            }

            if (searchDTO.userId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("userId"), searchDTO.userId()));
            }

            if (searchDTO.createdAfter() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        searchDTO.createdAfter()
                ));
            }

            if (searchDTO.createdBefore() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("createdAt"),
                        searchDTO.createdBefore()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
