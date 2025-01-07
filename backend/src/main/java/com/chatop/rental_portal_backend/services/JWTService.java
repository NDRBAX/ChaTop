package com.chatop.rental_portal_backend.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private final JwtDecoder jwtDecoder;
    private final JwtEncoder jwtEncoder;

    public JwtService(JwtDecoder jwtDecoder, JwtEncoder jwtEncoder) {
        this.jwtDecoder = jwtDecoder;
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(Authentication authentication) {
        logger.info("### Generating JWT token for user: {} ###", authentication.getName());

        Instant now = Instant.now();
        Instant expiration = now.plus(1, ChronoUnit.HOURS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuedAt(now)
                .expiresAt(expiration)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256)
                .type("JWT")
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(header,
                claims);

        return jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

    public boolean validateToken(String token) {
        try {
            jwtDecoder.decode(token);
            return true;
        } catch (JwtValidationException e) {
            logger.error("### Invalid JWT token: {} ###", token);
            return false;
        } catch (JwtException e) {
            logger.error("### Error during token validation: {} ###", e.getMessage());
            throw new RuntimeException("Unexpected error during token validation", e);
        }
    }

    public Jwt decodeToken(String token) {
        try {
            logger.info("### Decoding JWT token: {} ###", token);
            return jwtDecoder.decode(token);
        } catch (JwtException e) {
            logger.error("### Invalid JWT token: {} ###", token);
            throw new RuntimeException("Invalid token: " + e.getMessage(), e);
        }
    }

}
