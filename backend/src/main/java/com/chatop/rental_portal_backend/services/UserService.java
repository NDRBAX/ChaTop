package com.chatop.rental_portal_backend.services;

import org.springframework.stereotype.Service;

import com.chatop.rental_portal_backend.dto.AuthenticatedUserDTO;
import com.chatop.rental_portal_backend.mappers.AuthenticatedUserMapper;
import com.chatop.rental_portal_backend.models.User;
import com.chatop.rental_portal_backend.repositories.UserRepository;
import com.chatop.rental_portal_backend.services.impl.IUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final AuthenticatedUserMapper authenticatedUserMapper;

    public UserService(UserRepository userRepository, AuthenticatedUserMapper authenticatedUserMapper) {
        this.userRepository = userRepository;
        this.authenticatedUserMapper = authenticatedUserMapper;
    }

    @Override
    public AuthenticatedUserDTO getUserById(int id) {
        log.info(">>> GETTING USER BY ID: " + id);
        User user = userRepository.findById(id).orElse(null);
        return authenticatedUserMapper.userToAuthenticatedUserDTO(user);

    }
}
