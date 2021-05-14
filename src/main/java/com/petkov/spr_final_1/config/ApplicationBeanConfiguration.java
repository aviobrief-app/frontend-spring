package com.petkov.spr_final_1.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.petkov.spr_final_1.utils.ActiveTestTransporter;
import com.petkov.spr_final_1.utils.ValidationUtil;
import com.petkov.spr_final_1.utils.ValidationUtilImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class ApplicationBeanConfiguration {


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder().
                excludeFieldsWithoutExposeAnnotation().
                create();
    }

    @Bean
    public ValidationUtil validationUtil() {
        return new ValidationUtilImpl() {
        };
    }

//    @Bean
//    public ObjectMapper objectMapper(){
//        return new ObjectMapper();
//    }

    //TODO - ActiveTestTransporter Bean - change the scope/make prototype?
    //TODO - ActiveTestTransporter -  move somewhere else?
    @Bean
    @Scope("prototype")
    public ActiveTestTransporter activeTestTransporter() {
        return new ActiveTestTransporter();
    }
}
