package com.chatop.rental_portal_backend.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

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

import com.chatop.rental_portal_backend.exceptions.InvalidTokenException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtService implements IJwtService {

    private final JwtDecoder jwtDecoder;
    private final JwtEncoder jwtEncoder;

    public JwtService(JwtDecoder jwtDecoder, JwtEncoder jwtEncoder) {
        this.jwtDecoder = jwtDecoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public String generateToken(Authentication authentication) {
        log.info("### Generating JWT token for user: {} ###", authentication.getName());

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

    @Override
    public boolean validateToken(String token) {
        try {
            jwtDecoder.decode(token);
            return true;
        } catch (JwtValidationException e) {
            log.error("### Invalid JWT token: {} ###", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.error("### Error during token validation: {} ###", e.getMessage());
            throw new InvalidTokenException("Unexpected error during token validation");
        }
    }

    @Override
    public Jwt decodeToken(String token) {
        try {
            log.info("### Decoding JWT token: {} ###", token);
            return jwtDecoder.decode(token);
        } catch (JwtException e) {
            log.error("### Invalid JWT token: {} ###", e.getMessage());
            throw new InvalidTokenException("Invalid token");
        }
    }

}
