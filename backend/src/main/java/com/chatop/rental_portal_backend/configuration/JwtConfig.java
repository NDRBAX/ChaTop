package com.chatop.rental_portal_backend.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Configures the JWT decoder.
     * 
     * This method configures the JWT decoder to use the provided secret key and
     * algorithm to decode tokens.
     * 
     * @return the configured JWT decoder.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(this.jwtSecret.getBytes(), "HmacSHA256");
        log.info("### JWT DECODER CONFIGURATION ###");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }

    /**
     * Configures the JWT encoder.
     * 
     * This method configures the JWT encoder to use the provided secret key to
     * encode tokens.
     * 
     * @return the configured JWT encoder.
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        log.info("### JWT ENCODER CONFIGURATION ###");
        return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtSecret.getBytes()));
    }
}
