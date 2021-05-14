package com.petkov.spr_final_1.model.binding;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserLoginBindingModel {

    private String password;
    private String username;

    public UserLoginBindingModel() {
    }


    @NotBlank(message = "Password cannot be blank!")
    @Size(min = 3, max = 20, message = "Password must be between 3 and 20 characters!")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters!")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

