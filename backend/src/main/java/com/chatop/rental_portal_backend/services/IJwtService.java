package com.chatop.rental_portal_backend.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

public interface IJwtService {
    String generateToken(Authentication authentication);

    Jwt decodeToken(String token);
}
