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
import com.chatop.rental_portal_backend.exceptions.InvalidLoginRequestException;
import com.chatop.rental_portal_backend.exceptions.UserAlreadyExistsException;
import com.chatop.rental_portal_backend.exceptions.UserNotAuthenticatedException;
import com.chatop.rental_portal_backend.mappers.AuthenticatedUserMapper;
import com.chatop.rental_portal_backend.mappers.RegisterUserMapper;
import com.chatop.rental_portal_backend.models.User;
import com.chatop.rental_portal_backend.repositories.UserRepository;
import com.chatop.rental_portal_backend.services.impl.IAuthService;
import com.chatop.rental_portal_backend.services.impl.IJwtService;

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
            throw new UserAlreadyExistsException();
        }
        User user = registerUserMapper.registerRequestDTOToUser(registerRequestDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        log.info("### REGISTER USER ### User registered successfully with email {}", user.getEmail());
        Authentication authentication = authenticateUser(user.getEmail(), registerRequestDTO.getPassword());
        return generateToken(authentication);
    }

    @Override
    public String loginUser(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticateUser(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        return generateToken(authentication);
    }

    @Override
    public AuthenticatedUserDTO getAuthenticatedUser() {
        return getAuthenticatedUserFromContext()
                .map(authenticatedUserMapper::userToAuthenticatedUserDTO)
                .orElseThrow(() -> {
                    throw new UserNotAuthenticatedException();
                });

    }

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

    private String generateToken(Authentication authentication) {
        return jwtService.generateToken(authentication);
    }

}
