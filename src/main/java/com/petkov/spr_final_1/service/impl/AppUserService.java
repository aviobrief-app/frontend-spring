package com.petkov.spr_final_1.service.impl;

import com.petkov.spr_final_1.model.app_entity.UserEntity;
import com.petkov.spr_final_1.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppUserService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = this.userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with username: <" + username + "> not found!"));

        return mapToUserDetails(userEntity);
    }

    private UserDetails mapToUserDetails(UserEntity userEntity) {

        List<GrantedAuthority> authorities = userEntity
                .getRoles()
                .stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().name()))
                .collect(Collectors.toList());


        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                authorities
        );
    }
}
