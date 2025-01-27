package com.chatop.rental_portal_backend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CloudinaryConfig {

    @Value("${cloud.name}")
    private String CLOUD_NAME;

    @Value("${cloud.api_secret}")
    private String API_SECRET;

    @Value("${cloud.api_key}")
    private String API_KEY;

    /**
     * Configures the Cloudinary client.
     * 
     * This method configures the Cloudinary client to use the provided cloud name,
     * API key, and API secret.
     * 
     * @return the configured Cloudinary client.
     */
    @Bean
    public Cloudinary cloudinary() {
        log.info("### CLOUDINARY CONFIGURATION ###");
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET));
    }
}
