package com.chatop.rental_portal_backend.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {
    private static final Logger logger = LoggerFactory.getLogger(CloudinaryConfig.class);

    @Value("${cloud.name}")
    private String CLOUD_NAME;

    @Value("${cloud.api_secret}")
    private String API_SECRET;

    @Value("${cloud.api_key}")
    private String API_KEY;

    @Bean
    public Cloudinary cloudinary() {
        logger.info("### Configuring Cloudinary ###");
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET));
    }
}
