package com.expoai.bucket.repository;

import com.expoai.bucket.entity.ApiToken;
import com.expoai.bucket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiTokenRepository extends JpaRepository<ApiToken, Long> {

    Optional<ApiToken> findByTokenAndRevokedFalse(String token);
    Optional<ApiToken> findByUserAndRevokedFalse(User user);

}

