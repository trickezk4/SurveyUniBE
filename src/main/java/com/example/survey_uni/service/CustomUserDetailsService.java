package com.example.survey_uni.service;

import com.example.survey_uni.model.User;
import com.example.survey_uni.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService /*implements UserDetailsService */{

    @Autowired
    private UserRepository userRepository;

//    @Override
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword()) // đã mã hóa bằng BCrypt
                .roles(user.getRole().name()) // gán role
                .build();
    }
}
