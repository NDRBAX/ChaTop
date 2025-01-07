package com.chatop.rental_portal_backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.rental_portal_backend.dto.AuthenticatedUserDTO;
import com.chatop.rental_portal_backend.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get User by id
    @GetMapping("/{id}")
    public ResponseEntity<AuthenticatedUserDTO> getUserById(@Valid @PathVariable int id) {
        logger.info(">>> GETTING USER BY ID");
        AuthenticatedUserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

}
