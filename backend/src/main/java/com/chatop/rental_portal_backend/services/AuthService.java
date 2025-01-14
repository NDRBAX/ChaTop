package com.chatop.rental_portal_backend.services;

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
import com.chatop.rental_portal_backend.exceptions.InvalidCredentialsException;
import com.chatop.rental_portal_backend.exceptions.InvalidRequestException;
import com.chatop.rental_portal_backend.exceptions.UserAlreadyExistsException;
import com.chatop.rental_portal_backend.exceptions.UserNotAuthenticatedException;
import com.chatop.rental_portal_backend.mappers.AuthenticatedUserMapper;
import com.chatop.rental_portal_backend.mappers.RegisterUserMapper;
import com.chatop.rental_portal_backend.models.User;
import com.chatop.rental_portal_backend.repositories.UserRepository;

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

    @Override
    public String registerUser(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()) {
            log.error("### REGISTER USER ### Email {} is already in use", registerRequestDTO.getEmail());
            throw new UserAlreadyExistsException("");
        }

        User user = registerUserMapper.registerRequestDTOToUser(registerRequestDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        log.info("### REGISTER USER ### User registered successfully with email {}", user.getEmail());

        return authenticateUser(user.getEmail(), registerRequestDTO.getPassword())
                .map(this::generateToken)
                .orElseThrow(() -> {
                    log.error("### REGISTER USER ### Invalid credentials for email {}", user.getEmail());
                    throw new InvalidRequestException("Invalid credentials for email " + user.getEmail());
                });
    }

    @Override
    public String loginUser(LoginRequestDTO loginRequestDTO) {
        return authenticateUser(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
                .map(this::generateToken)
                .orElseThrow(() -> {
                    log.error("### LOGIN USER ### Invalid credentials for email {}", loginRequestDTO.getEmail());
                    throw new InvalidCredentialsException(
                            "Invalid credentials for email " + loginRequestDTO.getEmail());
                });
    }

    @Override
    public AuthenticatedUserDTO getAuthenticatedUser() throws UserNotAuthenticatedException {
        return getAuthenticatedUserFromContext()
                .map(authenticatedUserMapper::userToAuthenticatedUserDTO)
                .orElseThrow(() -> {
                    log.error("### GET AUTHENTICATED USER ### User not authenticated");
                    throw new UserNotAuthenticatedException("");
                });

    }

    private Optional<User> getAuthenticatedUserFromContext() {
        try {
            log.info("### GET AUTHENTICATED USER ### Getting authenticated user from context");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                log.error("### GET AUTHENTICATED USER ### No authenticated user found in context");
                return Optional.empty();
            }
            return Optional.of(authentication)
                    .map(Authentication::getName)
                    .flatMap(userRepository::findByEmail);
        } catch (AuthenticationException | JwtException ex) {
            log.error("Unable to get authenticated user. {}", ex.getMessage());
            return Optional.empty();
        }
    }

    private Optional<Authentication> authenticateUser(String email, String password) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));
            return Optional.of(authentication);
        } catch (AuthenticationException ex) {
            log.error("Unable to authenticate user. {}", ex.getMessage());
            return Optional.empty();
        }
    }

    private String generateToken(Authentication authentication) {
        return jwtService.generateToken(authentication);
    }

}
