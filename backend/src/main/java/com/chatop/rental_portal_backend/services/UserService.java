package com.chatop.rental_portal_backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chatop.rental_portal_backend.dto.AuthenticatedUserDTO;
import com.chatop.rental_portal_backend.mappers.AuthenticatedUserMapper;
import com.chatop.rental_portal_backend.models.User;
import com.chatop.rental_portal_backend.repositories.UserRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final AuthenticatedUserMapper authenticatedUserMapper;

    public UserService(UserRepository userRepository, AuthenticatedUserMapper authenticatedUserMapper) {
        this.userRepository = userRepository;
        this.authenticatedUserMapper = authenticatedUserMapper;
    }

    // Get user by id
    public AuthenticatedUserDTO getUserById(int id) {
        logger.info(">>> GETTING USER BY ID: " + id);

        User user = userRepository.findById(id).orElse(null);
        AuthenticatedUserDTO response = authenticatedUserMapper.userToAuthenticatedUserDTO(user);

        return response;

    }
}
