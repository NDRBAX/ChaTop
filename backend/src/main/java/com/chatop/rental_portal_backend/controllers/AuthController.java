package com.chatop.rental_portal_backend.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.rental_portal_backend.dto.AuthenticatedUserDTO;
import com.chatop.rental_portal_backend.dto.UserLoginDTO;
import com.chatop.rental_portal_backend.dto.UserRegisterDTO;
import com.chatop.rental_portal_backend.services.AuthService;
import com.chatop.rental_portal_backend.services.JWTService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    public final JWTService jwtService;
    private final AuthService authService;

    public AuthController(JWTService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String token = authService.loginUser(userLoginDTO);

            Map<String, String> response = Map.of("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Sinon -> erreur 401
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        try {
            // Enregistrement de l'utilisateur
            String token = authService.registerUser(userRegisterDTO);

            // Répondre avec un token JWT
            Map<String, String> response = Map.of("token", token);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<AuthenticatedUserDTO> getAuthenticatedUser() {
        try {
            AuthenticatedUserDTO authenticatedUserDTO = authService.getAuthenticatedUser();
            return new ResponseEntity<>(authenticatedUserDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
