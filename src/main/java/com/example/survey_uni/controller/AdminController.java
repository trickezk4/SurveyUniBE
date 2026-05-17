package com.example.survey_uni.controller;

import com.example.survey_uni.model.User;
import com.example.survey_uni.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create")
    public ResponseEntity<?> createAdmin() {
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setEmail("admin@gmail.com");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRole(User.Role.ADMIN);
            user.setStatus(User.Status.ACTIVE);
            user.setGender(User.Gender.MALE);
            userRepository.save(user);
            return ResponseEntity.ok("Admin account created!");
        }
        return ResponseEntity.badRequest().body("Admin already exists");
    }
}