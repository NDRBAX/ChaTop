package com.chatop.rental_portal_backend.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedUserDTO {
    private Integer id;
    private String name;
    private String email;
    private Timestamp created_at;
    private Timestamp updated_at;
}
