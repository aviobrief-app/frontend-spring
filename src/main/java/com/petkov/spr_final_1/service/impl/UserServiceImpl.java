package com.petkov.spr_final_1.service.impl;

import com.petkov.spr_final_1.model.app_entity.UserEntity;
import com.petkov.spr_final_1.model.app_entity.UserRoleEntity;
import com.petkov.spr_final_1.model.enumeration.UserRole;
import com.petkov.spr_final_1.model.service.UserServiceModel;
import com.petkov.spr_final_1.repository.UserRepository;
import com.petkov.spr_final_1.repository.UserRoleRepository;
import com.petkov.spr_final_1.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRoleRepository userRoleRepository, UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public long count() {
        return this.userRepository.count();
    }

    @Override
    public void seedUsers() {

        if (userRepository.count() == 0) {

            UserRoleEntity adminRole = new UserRoleEntity().setRole(UserRole.ADMIN);
            UserRoleEntity userRole = new UserRoleEntity().setRole(UserRole.USER);

            userRoleRepository.saveAll(List.of(adminRole, userRole));

            UserEntity admin = new UserEntity().setUsername("admin").setFullName("Petar Petkov").setPassword(passwordEncoder.encode("12345"));
            UserEntity user = new UserEntity().setUsername("user").setFullName("Elitsa Deyanova").setPassword(passwordEncoder.encode("12345"));
            admin.setRoles(List.of(adminRole, userRole));
            user.setRoles(List.of(userRole));

            userRepository.saveAll(List.of(admin, user));
        }
    }

    @Override
    public void saveUserToDatabase(UserServiceModel userServiceModel) {
        this.userRepository
                .saveAndFlush(this.modelMapper.map(userServiceModel, UserEntity.class));
    }

    @Override
    public UserServiceModel findByUsername(String username) {

        return this.userRepository
                .findByUsername(username)
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .orElse(null);
    }

    @Override
    public UserServiceModel findByEmail(String email) {
        return this.userRepository
                .findByEmail(email)
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .orElse(null);
    }
}
