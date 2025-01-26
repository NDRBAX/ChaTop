package com.chatop.rental_portal_backend.services.implementations;

import org.springframework.stereotype.Service;

import com.chatop.rental_portal_backend.dto.AuthenticatedUserDTO;
import com.chatop.rental_portal_backend.mappers.AuthenticatedUserMapper;
import com.chatop.rental_portal_backend.models.User;
import com.chatop.rental_portal_backend.repositories.UserRepository;
import com.chatop.rental_portal_backend.services.IUserService;

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

    /**
     * Retrieves a user by their ID.
     *
     * This method fetches a user entity from the repository by their ID, maps it to
     * an AuthenticatedUserDTO object, and returns the DTO. The DTO contains the
     * user's id, email, first name, last name, and role.
     *
     * @param id the ID of the user to retrieve.
     * @return an AuthenticatedUserDTO object representing the user.
     */
    @Override
    public AuthenticatedUserDTO getUserById(int id) {
        log.info(">>> GETTING USER BY ID: " + id);
        User user = userRepository.findById(id).orElse(null);
        return authenticatedUserMapper.userToAuthenticatedUserDTO(user);

    }
}
