package com.chatop.rental_portal_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.rental_portal_backend.dto.AuthenticatedUserDTO;
import com.chatop.rental_portal_backend.dto.EmptyResponseDTO;
import com.chatop.rental_portal_backend.services.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "Endpoints for user management")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @Operation(
        summary = "Get User by ID",
        description = "Fetches user details for a given user ID.", responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User details retrieved successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AuthenticatedUserDTO.class))
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EmptyResponseDTO.class))
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AuthenticatedUserDTO> getUserById(@Valid @PathVariable int id) {
        log.info(">>> GETTING USER BY ID");
        AuthenticatedUserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

}
