package com.chatop.rental_portal_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chatop.rental_portal_backend.dto.ResponseMessageDTO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // REGISTER -> 400
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(Exception ex) {
        log.error("User already exists >>> ERROR : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{}");
    }

    // LOGIN -> 401
    @ExceptionHandler(InvalidLoginRequestException.class)
    public ResponseEntity<ResponseMessageDTO> handleInvalidLoginRequestException(
            InvalidLoginRequestException invalidCredentialsException) {
        log.error("Unable to login >>> ERROR : {}", invalidCredentialsException.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseMessageDTO(invalidCredentialsException.getMessage()));
    }

    // GET AUTHENTICATED USER -> 401
    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<Object> handleUserNotAuthenticatedException(UserNotAuthenticatedException ex) {
        log.error("User is not authenticated >>> ERROR : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
                .body("{}");
    }

    // VALIDATION
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex,
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body("{}");
        } else if (requestURI.contains("/login")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessageDTO(errorMessage.toString()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDTO(errorMessage.toString()));
    }
}
