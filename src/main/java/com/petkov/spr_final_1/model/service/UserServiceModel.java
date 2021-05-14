package com.petkov.spr_final_1.model.service;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserServiceModel extends BaseServiceModel{

    private String username;
    private String password;
    private String email;

    //todo - UserServiceModel - check the usage of the class!

    public UserServiceModel(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserServiceModel() {
    }

    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 3, max = 20, message = "Username length must be between 3 and 20 characters!")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotBlank(message = "Password cannot be blank!")
    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 characters!")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Email(message = "Invalid email format!")
    @NotBlank(message = "Email cannot be blank!")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
