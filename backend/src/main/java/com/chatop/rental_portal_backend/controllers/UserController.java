package com.chatop.rental_portal_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.rental_portal_backend.dto.AuthenticatedUserDTO;
import com.chatop.rental_portal_backend.services.IUserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    // Get User by id
    @GetMapping("/{id}")
    public ResponseEntity<AuthenticatedUserDTO> getUserById(@Valid @PathVariable int id) {
        log.info(">>> GETTING USER BY ID");
        AuthenticatedUserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

}
