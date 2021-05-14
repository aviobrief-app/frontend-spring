package com.petkov.spr_final_1.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "cloudinary")
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;
    @Value("${cloudinary.api-key}")
    private String apiKey;
    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Bean
    public Cloudinary createCloudinaryConfig(){
        Map<String, Object> cloudinaryConfiguration = new HashMap<>();

        cloudinaryConfiguration.put("cloud_name", cloudName);
        cloudinaryConfiguration.put("api_key", apiKey);
        cloudinaryConfiguration.put("api_secret", apiSecret);

        return new Cloudinary(cloudinaryConfiguration);
    }

}
