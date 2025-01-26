package com.chatop.rental_portal_backend.services.implementations;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import com.chatop.rental_portal_backend.dto.AuthenticatedUserDTO;
import com.chatop.rental_portal_backend.dto.LoginRequestDTO;
import com.chatop.rental_portal_backend.dto.RegisterRequestDTO;
import com.chatop.rental_portal_backend.exceptions.InvalidLoginRequestException;
import com.chatop.rental_portal_backend.exceptions.UserAlreadyExistsException;
import com.chatop.rental_portal_backend.exceptions.UserNotAuthenticatedException;
import com.chatop.rental_portal_backend.mappers.AuthenticatedUserMapper;
import com.chatop.rental_portal_backend.mappers.RegisterUserMapper;
import com.chatop.rental_portal_backend.models.User;
import com.chatop.rental_portal_backend.repositories.UserRepository;
import com.chatop.rental_portal_backend.services.IAuthService;
import com.chatop.rental_portal_backend.services.IJwtService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    private final RegisterUserMapper registerUserMapper;
    private final AuthenticatedUserMapper authenticatedUserMapper;

    public AuthService(
            UserRepository userRepository,
            IJwtService jwtService,
            RegisterUserMapper registerUserMapper,
            AuthenticationManager authenticationManager,
            AuthenticatedUserMapper authenticatedUserMapper,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.registerUserMapper = registerUserMapper;
        this.authenticationManager = authenticationManager;
        this.authenticatedUserMapper = authenticatedUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user in the system.
     *
     * This method first checks if a user with the provided email already exists.
     * If the user exists, it throws a UserAlreadyExistsException. Otherwise, it
     * maps the RegisterRequestDTO to a User entity, encodes the user's password,
     * and saves the user to the repository. After successfully registering the user,
     * it logs the registration event, authenticates the user, and generates a JWT token.
     *
     * @param registerRequestDTO the data transfer object containing the user's registration details
     * @return a JWT token for the authenticated user
     * @throws UserAlreadyExistsException if a user with the provided email already exists
     */
    @Override
    public String registerUser(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = registerUserMapper.registerRequestDTOToUser(registerRequestDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        log.info("### REGISTER USER ### User registered successfully with email {}", user.getEmail());
        Authentication authentication = authenticateUser(user.getEmail(), registerRequestDTO.getPassword());
        return generateToken(authentication);
    }

    /**
     * Logs in a user.
     *
     * This method authenticates the user with the provided email and password and
     * generates a JWT token for the authenticated user.
     *
     * @param loginRequestDTO the data transfer object containing the user's login details
     * @return a JWT token for the authenticated user
     */
    @Override
    public String loginUser(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticateUser(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        return generateToken(authentication);
    }

    /**
     * Retrieves the authenticated user.
     *
     * This method retrieves the authenticated user from the SecurityContext and maps
     * the user entity to an AuthenticatedUserDTO. If no authenticated user is found,
     * it throws a UserNotAuthenticatedException.
     *
     * @return an AuthenticatedUserDTO containing the authenticated user's details
     * @throws UserNotAuthenticatedException if no authenticated user is found
     */
    @Override
    public AuthenticatedUserDTO getAuthenticatedUser() {
        return getAuthenticatedUserFromContext()
                .map(authenticatedUserMapper::userToAuthenticatedUserDTO)
                .orElseThrow(() -> {
                    throw new UserNotAuthenticatedException();
                });

    }

    /**
     * Retrieves the authenticated user from the SecurityContext.
     *
     * This method retrieves the authenticated user from the SecurityContext and returns
     * an Optional containing the user entity. If no authenticated user is found, it returns
     * an empty Optional.
     *
     * @return an Optional containing the authenticated user
     */
    private Optional<User> getAuthenticatedUserFromContext() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new UserNotAuthenticatedException();
            }
            return Optional.of(authentication)
                    .map(Authentication::getName)
                    .flatMap(userRepository::findByEmail);
        } catch (AuthenticationException | JwtException ex) {
            log.error("### GET AUTHENTICATED USER ### No authenticated user found in context");
            return Optional.empty();
        }

    }

    /**
     * Authenticates a user with the provided email and password.
     *
     * This method authenticates the user with the provided email and password using
     * the AuthenticationManager interface.
     *
     * @param email the email of the user to authenticate
     * @param password the password of the user to authenticate
     * @return the authenticated user
     * @throws InvalidLoginRequestException if the provided credentials are invalid
     */
    private Authentication authenticateUser(String email, String password) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));
            return authentication;
        } catch (AuthenticationException | JwtException ex) {
            log.error("Unable to authenticate user. {}", ex.getMessage());
            throw new InvalidLoginRequestException("Invalid credentials for email " + email);
        }
    }

    /**
     * Generates a JWT token for the authenticated user.
     *
     * This method generates a JWT token for the authenticated user using the
     * IJwtService interface.
     *
     * @param authentication the authenticated user
     * @return a JWT token for the authenticated user
     */
    private String generateToken(Authentication authentication) {
        return jwtService.generateToken(authentication);
    }

}
