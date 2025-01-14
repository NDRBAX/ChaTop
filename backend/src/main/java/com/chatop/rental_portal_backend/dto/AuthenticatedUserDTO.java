package com.chatop.rental_portal_backend.dto;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuthenticatedUserDTO {
    @Schema(description = "The user's ID", example = "1")
    private Integer id;

    @Schema(description = "The user's name", example = "John Doe")
    private String name;

    @Schema(description = "The user's email", example = "john_doe@mail.com")
    private String email;

    @Schema(description = "Account creation date", example = "2025-01-01 12:00:00")
    private Timestamp created_at;

    @Schema(description = "Last account update date", example = "2025-01-01 12:00:00")
    private Timestamp updated_at;
}
