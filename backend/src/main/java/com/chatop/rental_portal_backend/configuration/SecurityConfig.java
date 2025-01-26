package com.chatop.rental_portal_backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.chatop.rental_portal_backend.security.ChatopAuthenticationEntryPoint;
import com.chatop.rental_portal_backend.services.IJwtService;
import com.chatop.rental_portal_backend.services.implementations.UserDetailsLoaderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_PATHS = {
        "/api/auth/register",
            "/api/auth/login",
            "/swagger-ui/**",
            "/v3/api-docs/**",
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

    /**
     * Configures the security filter chain.
     * 
     * This method configures the security filter chain to allow public paths to be
     * accessed without authentication and to require authentication for all other
     * paths. It also configures the OAuth2 resource server to use the provided JWT
     * decoder to decode tokens.
     * 
     * @param http the HttpSecurity object to configure.
     * @return the SecurityFilterChain object.
     * @throws Exception if an error occurs while configuring the security filter
     *                  chain.  
     */
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

    /**
    * Configures the authentication manager.
    * 
    * This method configures the authentication manager to use the provided user
    * details loader service and password encoder.
    * 
    * @param http the HttpSecurity object to configure.
    * @return the AuthenticationManager object.
    * @throws Exception if an error occurs while configuring the authentication
    *                   manager.
    */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        log.info("### AUTHENTICATION MANAGER CONFIGURATION ###");
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(userDetailsLoaderService)
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }


    /**
     * Configures the password encoder.
     * 
     * This method configures the password encoder to use the BCrypt algorithm.
     * 
     * @return the PasswordEncoder object.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("### BCRYPT PASSWORD ENCODER CONFIGURATION ###");
        return new BCryptPasswordEncoder();
    }

}