package com.chatop.rental_portal_backend.controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        String token = authService.registerUser(userRegisterDTO);
        Map<String, String> response = Map.of("token", token);
        logger.info(">>> USER REGISTERED WITH EMAIL >>> {} <<<", userRegisterDTO.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        String token = authService.loginUser(userLoginDTO);
        Map<String, String> response = Map.of("token", token);
        logger.info(">>> USER LOGGED IN WITH EMAIL >>> {} <<<", userLoginDTO.getEmail());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<AuthenticatedUserDTO> getAuthenticatedUser() {
        AuthenticatedUserDTO authenticatedUserDTO = authService.getAuthenticatedUser();
        logger.info(">>> GETTING AUTHENTICATED USER WITH EMAIL >>> {} <<<",
                authenticatedUserDTO.getEmail());
        return new ResponseEntity<>(authenticatedUserDTO, HttpStatus.OK);
    }
}
