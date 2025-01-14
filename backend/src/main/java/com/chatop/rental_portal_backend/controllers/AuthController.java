package com.chatop.rental_portal_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.rental_portal_backend.dto.AuthSuccessDTO;
import com.chatop.rental_portal_backend.dto.AuthenticatedUserDTO;
import com.chatop.rental_portal_backend.dto.LoginRequestDTO;
import com.chatop.rental_portal_backend.dto.RegisterRequestDTO;
import com.chatop.rental_portal_backend.services.IAuthService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthSuccessDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        String token = authService.registerUser(registerRequest);
        log.info(">>> USER REGISTERED WITH EMAIL >>> {} <<<", registerRequest.getEmail());
        return ResponseEntity.ok(new AuthSuccessDTO(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthSuccessDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        String token = authService.loginUser(loginRequest);
        log.info(">>> USER LOGGED IN WITH EMAIL >>> {} <<<", loginRequest.getEmail());
        return ResponseEntity.ok(new AuthSuccessDTO(token));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthenticatedUserDTO> getAuthenticatedUser() {
        AuthenticatedUserDTO authenticatedUserDTO = authService.getAuthenticatedUser();

        log.info(">>> GETTING AUTHENTICATED USER WITH EMAIL >>> {} <<<",
                authenticatedUserDTO.getEmail());
        return ResponseEntity.ok(authenticatedUserDTO);
    }
}
