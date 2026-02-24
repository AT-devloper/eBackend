package com.example.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dxwuv127h",
            "api_key", "382752924912177",
            "api_secret", "fHlPhM08vX6kYBMxUGM2jM9udI0",
            "secure", true
        ));
    }
}


