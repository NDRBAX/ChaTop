package com.chatop.rental_portal_backend.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import com.chatop.rental_portal_backend.dto.AuthenticatedUserDTO;
import com.chatop.rental_portal_backend.dto.UserLoginDTO;
import com.chatop.rental_portal_backend.dto.UserRegisterDTO;
import com.chatop.rental_portal_backend.mappers.AuthenticatedUserMapper;
import com.chatop.rental_portal_backend.mappers.RegisterUserMapper;
import com.chatop.rental_portal_backend.models.User;
import com.chatop.rental_portal_backend.repositories.UserRepository;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    private final RegisterUserMapper registerUserMapper;
    private final AuthenticatedUserMapper authenticatedUserMapper;

    public AuthService(
            UserRepository userRepository,
            JwtService jwtService,
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

    public String registerUser(UserRegisterDTO userRegisterDTO) {
        if (userRepository.findByEmail(userRegisterDTO.getEmail()).isPresent()) {
            logger.error("### REGISTER USER ### Email {} is already in use", userRegisterDTO.getEmail());
            throw new IllegalArgumentException("");
        }

        User user = registerUserMapper.userRegisterDTOToUser(userRegisterDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        logger.info("### REGISTER USER ### User registered successfully with email {}", user.getEmail());

        return authenticateUser(user.getEmail(), userRegisterDTO.getPassword())
                .map(this::generateToken)
                .orElseThrow(() -> {
                    logger.error("### REGISTER USER ### Invalid credentials for email {}", user.getEmail());
                    throw new RuntimeException("Invalid credentials for email " + user.getEmail());
                });
    }

    public String loginUser(UserLoginDTO userLoginDTO) {
        return authenticateUser(userLoginDTO.getEmail(), userLoginDTO.getPassword())
                .map(this::generateToken)
                .orElseThrow(() -> {
                    logger.error("### LOGIN USER ### Invalid credentials for email {}", userLoginDTO.getEmail());
                    throw new IllegalArgumentException("Invalid credentials for email " + userLoginDTO.getEmail());
                });
    }

    public AuthenticatedUserDTO getAuthenticatedUser() {
        return getAuthenticatedUserFromContext()
                .map(authenticatedUserMapper::userToAuthenticatedUserDTO)
                .orElseThrow(() -> {
                    logger.error("### GET AUTHENTICATED USER ### User not authenticated");
                    throw new IllegalArgumentException("");
                });

    }

    private Optional<User> getAuthenticatedUserFromContext() {
        try {
            return Optional.of(SecurityContextHolder.getContext().getAuthentication())
                    .map(Authentication::getName)
                    .flatMap(userRepository::findByEmail);
        } catch (AuthenticationException | JwtException ex) {
            return Optional.empty();
        }
    }

    private Optional<Authentication> authenticateUser(String email, String password) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));
            return Optional.of(authentication);
        } catch (AuthenticationException ex) {
            return Optional.empty();
        }
    }

    private String generateToken(Authentication authentication) {
        return jwtService.generateToken(authentication);
    }
}
