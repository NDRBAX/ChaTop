package com.chatop.rental_portal_backend.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.chatop.rental_portal_backend.services.JwtService;
import com.chatop.rental_portal_backend.services.UserDetailsLoaderService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final UserDetailsLoaderService userDetailsLoaderService;
    private final JwtService jwtService;

    public SecurityConfig(UserDetailsLoaderService userDetailsLoaderService, JwtService jwtService) {
        this.userDetailsLoaderService = userDetailsLoaderService;
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("### SECURITY FILTER CHAIN CONFIGURATION ###");
        return http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/auth/register", "/api/auth/login"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtService::decodeToken)))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        logger.info("### AUTHENTICATION MANAGER CONFIGURATION ###");
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(userDetailsLoaderService)
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        logger.info("### BCRYPT PASSWORD ENCODER CONFIGURATION ###");
        return new BCryptPasswordEncoder();
    }
}