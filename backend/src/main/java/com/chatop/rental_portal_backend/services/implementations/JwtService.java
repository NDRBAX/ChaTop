package com.chatop.rental_portal_backend.services.implementations;

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
import org.springframework.stereotype.Service;

import com.chatop.rental_portal_backend.services.IJwtService;

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

    /**
     * Generates a JWT token.
     * 
     * This method generates a JWT token for the provided authentication object.
     * 
     * @param authentication the authentication object to generate the token for.
     * @return the generated JWT token.
     */
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

    /**
     * Decodes a JWT token.
     * 
     * This method decodes the provided JWT token and returns the decoded JWT object.
     * 
     * @param token the JWT token to decode.
     * @return the decoded JWT object.
     */
    @Override
    public Jwt decodeToken(String token) {
        return jwtDecoder.decode(token);
    }

}
