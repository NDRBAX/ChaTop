package com.chatop.rental_portal_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required and cannot be blank")
    @Schema(description = "The email address of the user. This field is mandatory and must be a valid email format.", example = "john_doe@mail.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "The password of the user. This field is mandatory.", example = "password123")
    private String password;
}
