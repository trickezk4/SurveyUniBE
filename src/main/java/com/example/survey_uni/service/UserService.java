package com.example.survey_uni.service;

import com.example.survey_uni.dto.UserDTO;
import com.example.survey_uni.dto.request.RegisterRequest;
import com.example.survey_uni.model.User;
import com.example.survey_uni.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers(){
        return userRepository.findAll().stream()
                .map(u -> new UserDTO(u.getUserId(), u.getEmail(), u.getUsername(), u.getRole(), u.getStatus(), u.getGender()))
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found!"));
        return new UserDTO(user.getUserId(), user.getEmail(), user.getUsername(), user.getRole(), user.getStatus(), user.getGender());
    }

    public UserDTO createUser(RegisterRequest request){
        User saved = new User();

        saved.setUsername(request.getUsername());
        saved.setEmail(request.getEmail());
        saved.setPassword(passwordEncoder.encode(request.getPassword()));
        saved.setStatus(User.Status.valueOf(request.getStatus()));
        saved.setGender(User.Gender.valueOf(request.getGender()));
        saved.setRole(User.Role.valueOf(request.getRole()));
        userRepository.save(saved);

        return new UserDTO(saved.getUserId(), saved.getEmail(), saved.getUsername(), saved.getRole(), saved.getStatus(), saved.getGender());
    }

    public UserDTO updateUser(Integer id, User userUpdate) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        user.setUsername(userUpdate.getUsername());
        user.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
        user.setRole(userUpdate.getRole());

        User updated = userRepository.save(user);
        return new UserDTO(updated.getUserId(), updated.getEmail(), updated.getUsername(), updated.getRole(), updated.getStatus(), updated.getGender());
    }

    public UserDTO deleteUser(Integer id){
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        userRepository.deleteById(id);
        return new UserDTO(user.getUserId(), user.getEmail(), user.getUsername(), user.getRole(), user.getStatus(), user.getGender());
    }


}
