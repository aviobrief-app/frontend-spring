package com.petkov.spr_final_1.service;


import com.petkov.spr_final_1.model.service.UserServiceModel;

public interface UserService {
    long count();

    void seedUsers();

    void saveUserToDatabase(UserServiceModel userServiceModel);

    UserServiceModel findByUsername(String username);

    UserServiceModel findByEmail(String email);
}
