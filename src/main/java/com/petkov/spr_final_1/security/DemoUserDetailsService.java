package com.petkov.spr_final_1.security;

import com.petkov.spr_final_1.model.app_entity.UserEntity;
import com.petkov.spr_final_1.repository.UserRepository;
import com.petkov.spr_final_1.repository.UserRoleRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class DemoUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public DemoUserDetailsService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Error!"));

        return mapToUserDetails(userEntity);
    }

    private UserDetails mapToUserDetails(UserEntity userEntity) {
//        user
//                .getRoles()
//                .stream()
//                .map(userRole -> new SimpleGrantedAuthority( ))

//        org.springframework.security.core.userdetails.User result
//                = new org.springframework.security.core.userdetails.User(
//                user.getUsername(),
//                user.getPassword(),
//
//
//                );

        return null;

    }
}
