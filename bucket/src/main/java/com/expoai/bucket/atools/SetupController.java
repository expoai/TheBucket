package com.expoai.bucket.atools;

import com.expoai.bucket.entity.Role;
import com.expoai.bucket.entity.User;
import com.expoai.bucket.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setup")
public class SetupController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public SetupController(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = encoder;
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody AdminRequest request) {
        if (userRepo.count() > 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Admin already exists");
        }

        User admin = new User();
        admin.setUsername(request.username());
        admin.setPassword(passwordEncoder.encode(request.password()));
        Role role = new Role () ;
        role.setId(1L);
        admin.setRoles(List.of(role));

        userRepo.save(admin);
        return ResponseEntity.ok("Admin created successfully");
    }

    public record AdminRequest(String username, String password) {}
}
