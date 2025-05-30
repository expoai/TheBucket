package com.expoai.bucket.service;

import com.expoai.bucket.dto.outward.StudentGroupDTO;
import com.expoai.bucket.entity.User;
import com.expoai.bucket.enums.TokenType;
import com.expoai.bucket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    final ApiTokenService apiTokenService ;
    final UserRepository userRepository;

    public StudentGroupDTO createStudentGroup(String groupName) {

        User user = User
                .builder()
                .username(groupName)
                .password(UUID.randomUUID().toString())
                .roles(List.of())
                .build();

        userRepository.save(user);

        return StudentGroupDTO
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .apiKey(apiTokenService.getApiToken(user.getUsername(), TokenType.STUDENT))
                .build() ;
    }

}
