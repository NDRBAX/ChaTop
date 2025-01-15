package com.chatop.rental_portal_backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.chatop.rental_portal_backend.security.ChatopAuthenticationEntryPoint;
import com.chatop.rental_portal_backend.services.UserDetailsLoaderService;
import com.chatop.rental_portal_backend.services.impl.IJwtService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
        private static final String[] PUBLIC_PATHS = {
                        "/api/auth/register",
                        "/api/auth/login",
                        "/api/auth/me",
                        "/swagger-ui/**",
                        "/api-docs/**"
        };

        private final UserDetailsLoaderService userDetailsLoaderService;
        private final IJwtService jwtService;
        private final ChatopAuthenticationEntryPoint chatopAuthenticationEntryPoint;

        public SecurityConfig(UserDetailsLoaderService userDetailsLoaderService, IJwtService jwtService,
                        ChatopAuthenticationEntryPoint chatopAuthenticationEntryPoint) {
                this.userDetailsLoaderService = userDetailsLoaderService;
                this.jwtService = jwtService;
                this.chatopAuthenticationEntryPoint = chatopAuthenticationEntryPoint;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                log.info("### SECURITY FILTER CHAIN CONFIGURATION ###");
                return http
                                .csrf(csrf -> csrf.ignoringRequestMatchers(PUBLIC_PATHS))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                                .requestMatchers(PUBLIC_PATHS).permitAll()
                                                .anyRequest().authenticated())
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(jwt -> jwt.decoder(jwtService::decodeToken))
                                                .authenticationEntryPoint(chatopAuthenticationEntryPoint)

                                )
                                .build();
        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                log.info("### AUTHENTICATION MANAGER CONFIGURATION ###");
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);

                authenticationManagerBuilder.userDetailsService(userDetailsLoaderService)
                                .passwordEncoder(passwordEncoder());

                return authenticationManagerBuilder.build();
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                log.info("### BCRYPT PASSWORD ENCODER CONFIGURATION ###");
                return new BCryptPasswordEncoder();
        }

}