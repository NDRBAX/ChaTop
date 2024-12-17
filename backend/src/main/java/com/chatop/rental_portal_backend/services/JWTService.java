package com.chatop.rental_portal_backend.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JWTService {
    private final JwtEncoder jwtEncoder;

    public JWTService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(Authentication authentication) {
        // Calcule la date d'expiration du token en ajoutant 1 heure à l'instant actuel
        Instant now = Instant.now();
        Instant expiration = now.plus(1, ChronoUnit.HOURS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                // Définit le sujet du JWT, souvent l'identifiant unique de l'utilisateur
                .subject(authentication.getName())
                // Ajoute une revendication pour indiquer la date d'émission du token
                .issuedAt(now)
                // Ajoute une revendication pour indiquer la date d'expiration du token
                .expiresAt(expiration)
                // Construit l'objet JwtClaimsSet qui contient les informations du JWT
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256)
                .type("JWT")
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(header,
                claims);

        return jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
}
