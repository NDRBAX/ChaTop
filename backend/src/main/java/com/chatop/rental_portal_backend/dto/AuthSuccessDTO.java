package com.chatop.rental_portal_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthSuccessDTO {
    private String token;
}
