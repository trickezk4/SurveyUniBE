package com.example.survey_uni.dto;

import com.example.survey_uni.model.User;

public class UserDTO {
    private Integer userId;
    private String email;
    private String username;
    private User.Role role;
    private User.Status status;
    private User.Gender gender;


    public UserDTO(Integer userId, String email, String username, User.Role role, User.Status status, User.Gender gender) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.role = role;
        this.status = status;
        this.gender = gender;
    }

    //Getter & Setter
    public Integer getUserId(){
        return userId;
    }
    public void setUserId(Integer userId){
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User.Status getStatus() {
        return status;
    }

    public void setStatus(User.Status status) {
        this.status = status;
    }

    public User.Gender getGender() {
        return gender;
    }

    public void setGender(User.Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "userId: " + userId +
                "\nusername: " + username + '\'' +
                "\nrole: " + role;
    }

    public static String notFoundString(){
        return "User not found!";
    }
}
