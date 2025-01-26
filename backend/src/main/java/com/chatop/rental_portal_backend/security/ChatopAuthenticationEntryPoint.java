package com.chatop.rental_portal_backend.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.chatop.rental_portal_backend.dto.ResponseMessageDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChatopAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * This method is called whenever an unauthenticated user tries to access a
     * protected resource.
     *
     * This method sends an HTTP response with the appropriate status code and
     * message based on the request URI.
     *
     * @param request       the HTTP request.
     * @param response      the HTTP response.
     * @param authException the exception that caused the authentication failure.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        String requestURI = request.getRequestURI();

        if (requestURI.contains("/api/auth/me")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{}");
            log.error(authException.getMessage());
        } else if (requestURI.contains("/api/auth/register")) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
            response.getWriter().write("{}");
            log.error(authException.getMessage());
        } else if (requestURI.contains("/api/auth/login")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write(new com.fasterxml.jackson.databind.ObjectMapper()
                    .writeValueAsString(new ResponseMessageDTO(authException.getMessage())));
            log.error(authException.getMessage());
        } else if (requestURI.contains("/api/rentals") || requestURI.contains("/api/users/")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("");
            log.error(authException.getMessage());
        }
    }

}
