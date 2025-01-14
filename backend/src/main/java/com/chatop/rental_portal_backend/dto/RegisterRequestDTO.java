package com.chatop.rental_portal_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "Name is required")
    @Schema(description = "The name of the user. This field is mandatory.", example = "John Doe")
    private String name;

    @Email
    @NotBlank(message = "Email is required")
    @Schema(description = "The email address of the user. This field is mandatory and must be a valid email format.", example = "john_doe@mail.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "The password of the user. This field is mandatory.", example = "password123")
    private String password;
}
