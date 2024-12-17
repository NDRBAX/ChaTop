package com.chatop.rental_portal_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
