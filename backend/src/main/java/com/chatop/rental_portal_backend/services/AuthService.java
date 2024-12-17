package com.chatop.rental_portal_backend.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    /**
     * Déclaration des dépendances nécessaires pour le service utilisateur
     * 
     * @UserRepository permet d'interagir avec la base de données pour les entités
     *                 User
     * @UserMapper permet de convertir les objets UserRegisterDTO en objets User
     */
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    private final RegisterUserMapper registerUserMapper;
    private final AuthenticatedUserMapper authenticatedUserMapper;

    public AuthService(
            UserRepository userRepository,
            JWTService jwtService,
            RegisterUserMapper registerUserMapper,
            AuthenticationManager authenticationManager,
            AuthenticatedUserMapper authenticatedUserMapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.registerUserMapper = registerUserMapper;
        this.authenticationManager = authenticationManager;
        this.authenticatedUserMapper = authenticatedUserMapper;
    }

    /**
     * Méthode permettant d'enregistrer un utilisateur dans la base de données
     * 
     * @param userRegisterDTO
     * @return String token
     */
    public String registerUser(UserRegisterDTO userRegisterDTO) {
        // Vérification si l'email existe déjà
        Optional<User> existingUser = userRepository.findByEmail(userRegisterDTO.getEmail());

        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Convertir le DTO en User
        User user = registerUserMapper.userRegisterDTOToUser(userRegisterDTO);

        // Enregistrer l'utilisateur dans la base de données
        userRepository.save(user);

        // Générer un JWT pour l'utilisateur
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        return jwtService.generateToken(authentication);
    }

    public String loginUser(UserLoginDTO userLoginDTO) {
        try {
            // Authentifier l'utilisateur avec ses informations d'identification
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword()));

            // Si l'authentification réussit -> générer un token JWT
            String token = jwtService.generateToken(authentication);

            return token;
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    public AuthenticatedUserDTO getAuthenticatedUser() {
        // Récupérer l'utilisateur depuis SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("User is not authenticated");
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Mapper le modèle User vers le DTO AuthenticatedUserDTO
        return authenticatedUserMapper.userToAuthenticatedUserDTO(user);
    }

}
