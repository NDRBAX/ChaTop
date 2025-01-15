package com.chatop.rental_portal_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.rental_portal_backend.dto.AuthSuccessDTO;
import com.chatop.rental_portal_backend.dto.AuthenticatedUserDTO;
import com.chatop.rental_portal_backend.dto.EmptyResponseDTO;
import com.chatop.rental_portal_backend.dto.LoginRequestDTO;
import com.chatop.rental_portal_backend.dto.RegisterRequestDTO;
import com.chatop.rental_portal_backend.dto.ResponseMessageDTO;
import com.chatop.rental_portal_backend.services.impl.IAuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Endpoints for user authentication")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @Operation(
        summary = "Register a new user",
        description = "Registers a new user in the system.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User registered successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AuthSuccessDTO.class),
                examples = @ExampleObject(value = """
                    {
                    "token" : "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MzY5NjQyMDAsInN1YiI6InRlc3RAbWFpbC5jb20iLCJpYXQiOjE3MzY5NjA2MDB9.nTeRgSgFXIBqucayyjNbVYEBwv4ZYBn6cTDZS7tVz-k"
                    }
                    """,
                    summary = "User registered successfully"))),
            @ApiResponse(
                responseCode = "400",
                description = "User cannot be registred with the provided data",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EmptyResponseDTO.class),
                examples = @ExampleObject(value = """
                    {}
                    """,
                    summary = "User cannot be registered with the provided data")))
    })
    @Parameter(
        name = "registerRequest",
        description = "The user registration request",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = RegisterRequestDTO.class)))
    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        String token = authService.registerUser(registerRequest);
        log.info(">>> USER REGISTERED WITH EMAIL >>> {} <<<", registerRequest.getEmail());
        return ResponseEntity.ok(new AuthSuccessDTO(token));
    }

    @Operation(
        summary = "Login a user",
        description = "Logs in a user in the system.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User logged in successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AuthSuccessDTO.class),
                examples = @ExampleObject(value = """
                    {
                    "token" : "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MzY5NjQyMDAsInN1YiI6InRlc3RAbWFpbC5jb20iLCJpYXQiOjE3MzY5NjA2MDB9.nTeRgSgFXIBqucayyjNbVYEBwv4ZYBn6cTDZS7tVz-k"
                    }
                    """,
                    summary = "User logged in successfully"))),

            @ApiResponse(
                responseCode = "401",
                description = "Unable to login user with provided credentials",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResponseMessageDTO.class),
                examples = @ExampleObject(value = """
                    {
                    "message" : "Unable to login user with provided credentials"
                    }
                    """,
                    summary = "Unable to login user with provided credentials")))
    })
    @Parameter(
        name = "loginRequest",
        description = "The user login request",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = LoginRequestDTO.class)))
    @PostMapping("/login")
    public ResponseEntity<AuthSuccessDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        String token = authService.loginUser(loginRequest);
        log.info(">>> USER LOGGED IN WITH EMAIL >>> {} <<<", loginRequest.getEmail());
        return ResponseEntity.ok(new AuthSuccessDTO(token));
    }

    @Operation(
        summary = "Get authenticated user",
        description = "Fetches the authenticated user details.",
        responses = {
            @ApiResponse(responseCode = "200",
            description = "User details retrieved successfully",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = AuthenticatedUserDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "name": "John Doe",
                        "email": "john_doe@mail.com",
                        "created_at": "2025-01-01 12:00:00",
                        "updated_at": "2025-01-01 12:00:00"
                    }
                    """,
                    summary = "User details retrieved successfully"))),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EmptyResponseDTO.class),
                examples = @ExampleObject(value = """
                    {}
                    """,
                    summary = "Unauthorized")))
    })
    @Parameter(
        name = "authenticatedUser",
        description = "The authenticated user details",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = AuthenticatedUserDTO.class)))
    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/me")
    public ResponseEntity<AuthenticatedUserDTO> getAuthenticatedUser() {
        AuthenticatedUserDTO authenticatedUserDTO = authService.getAuthenticatedUser();
        log.info(">>> GETTING AUTHENTICATED USER WITH EMAIL >>> {} <<<",
                authenticatedUserDTO.getEmail());
        return ResponseEntity.ok(authenticatedUserDTO);
    }
}
