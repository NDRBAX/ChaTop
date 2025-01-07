package com.chatop.rental_portal_backend.exceptions;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // REGISTER
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler({ UserAlreadyExistsException.class, InvalidRequestException.class })
    public Map<String, String> handleUserAlreadyExistsException() {
        return Collections.emptyMap();
    }

    // LOGIN
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    @ExceptionHandler(InvalidCredentialsException.class)
    public Map<String, String> handleInvalidCredentialsException(
            InvalidCredentialsException invalidCredentialsException) {
        return Map.of("message",
                invalidCredentialsException.getMessage());
    }

    // GET AUTHENTICATED USER
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    @ExceptionHandler(UserNotAuthenticatedException.class)
    public Map<String, String> handleBadCredentialsException() {
        return Collections.emptyMap();
    }

    // VALIDATION
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        StringBuilder errorMessage = new StringBuilder();

        // Get errors -> then concatenate
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            if (errorMessage.length() > 0) {
                errorMessage.append(" ");
            }
            errorMessage.append(error.getDefaultMessage()).append(".");
        });

        String requestURI = request.getRequestURI();

        if (requestURI.contains("/register") || requestURI.contains("/me")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyMap());
        } else if (requestURI.contains("/login")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", errorMessage.toString()));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", errorMessage.toString()));
    }
}
