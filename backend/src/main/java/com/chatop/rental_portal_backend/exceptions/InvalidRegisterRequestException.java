package com.chatop.rental_portal_backend.exceptions;

public class InvalidRegisterRequestException extends RuntimeException {
    public InvalidRegisterRequestException(String message) {
        super(message);
    }
}