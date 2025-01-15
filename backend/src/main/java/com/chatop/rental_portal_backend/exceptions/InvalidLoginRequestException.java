package com.chatop.rental_portal_backend.exceptions;

public class InvalidLoginRequestException extends RuntimeException {
    public InvalidLoginRequestException(String message) {
        super(message);
    }

}
