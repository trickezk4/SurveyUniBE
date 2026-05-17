package com.example.survey_uni.controller;


import com.example.survey_uni.dto.UserDTO;
import com.example.survey_uni.dto.request.RegisterRequest;
import com.example.survey_uni.model.User;
import com.example.survey_uni.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers(Authentication auth){
        System.out.println("Role all:" + auth.getAuthorities());
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Integer id){
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDTO createUser(@RequestBody RegisterRequest request){
        return userService.createUser(request);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Integer id, @RequestBody User userUpdate){
        return userService.updateUser(id, userUpdate);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Integer id){
        String deletedUsername = userService.deleteUser(id).getUsername();
        return "User " + "\"" + deletedUsername + "\"" + " deleted successfully!\n" ;
    }

}
