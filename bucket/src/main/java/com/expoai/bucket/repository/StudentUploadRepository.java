package com.expoai.bucket.repository;

import com.expoai.bucket.entity.StudentUpload;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface StudentUploadRepository extends Repository<StudentUpload, Long> {

    Optional<StudentUpload> findByIdExterne(Long aLong);

    <S extends StudentUpload> S save(S entity);

    @Query("""
        SELECT s FROM StudentUpload s
        WHERE 
          (:tag1 IS NULL OR s.tag1 = :tag1) AND
          (:tag2 IS NULL OR s.tag2 = :tag2) AND
          (:tag3 IS NULL OR s.tag3 = :tag3) AND 
          :idExterne = :idExterne
    """)
    List<StudentUpload> findByTags(
        @Param("tag1") String tag1,
        @Param("tag2") String tag2,
        @Param("tag3") String tag3,
        @Param("idExterne") String idExterne
    );
}