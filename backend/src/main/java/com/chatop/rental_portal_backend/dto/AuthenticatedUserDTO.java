package com.chatop.rental_portal_backend.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class AuthenticatedUserDTO {
    private Integer id;
    private String name;
    private String email;
    private Timestamp created_at;
    private Timestamp updated_at;
}
