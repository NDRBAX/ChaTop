package com.chatop.rental_portal_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.chatop.rental_portal_backend.dto.EmptyResponseDTO;
import com.chatop.rental_portal_backend.dto.ResponseMessageDTO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // REGISTER
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler({ UserAlreadyExistsException.class, InvalidRequestException.class })
    public EmptyResponseDTO handleUserAlreadyExistsException() {
        return new EmptyResponseDTO();
    }

    // LOGIN
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseMessageDTO handleInvalidCredentialsException(
            InvalidCredentialsException invalidCredentialsException) {
        return new ResponseMessageDTO(invalidCredentialsException.getMessage());
    }

    // GET AUTHENTICATED USER
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    @ExceptionHandler({ UserNotAuthenticatedException.class, InvalidTokenException.class })
    public EmptyResponseDTO handleBadCredentialsException(InvalidTokenException ex) {
        log.error("Handling invalid token exception: {}", ex.getMessage());
        return new EmptyResponseDTO();
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new EmptyResponseDTO());
        } else if (requestURI.contains("/login")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessageDTO(errorMessage.toString()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDTO(errorMessage.toString()));
    }
}
