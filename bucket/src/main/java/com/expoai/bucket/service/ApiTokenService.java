package com.expoai.bucket.service;

import com.expoai.bucket.entity.ApiToken;
import com.expoai.bucket.entity.User;
import com.expoai.bucket.repository.ApiTokenRepository;
import com.expoai.bucket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiTokenService {

    private final ApiTokenRepository tokenRepo;
    private final UserRepository userRepo;

    public String getApiToken(String username) {

        User user = userRepo.findByUsername(username);

        // Generate and save new token
        String token = UUID.randomUUID().toString();

        ApiToken apiToken = new ApiToken();
        apiToken.setUser(user);
        apiToken.setToken(token);

        tokenRepo.save(apiToken);
        return token ;
    }

}
