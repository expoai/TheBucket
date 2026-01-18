package com.expoai.bucket.service;

import com.expoai.bucket.entity.Role;
import com.expoai.bucket.entity.User;
import com.expoai.bucket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User registerNewUserAccount(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<String> getUserRoles(String username) {
        User user = getUser(username);
        return getUserRoles(String.valueOf(user));
    }

    public List<String> getUserRoles(User user) {
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

}
