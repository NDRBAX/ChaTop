package com.chatop.rental_portal_backend.dto;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateRentalDTO {
    @Schema(description = "The rental's name", example = "Cozy apartment")
    private String name;

    @Schema(description = "The rental's price", example = "1000.0")
    private double price;

    @Schema(description = "The rental's description", example = "A cozy apartment in the heart of the city")
    private String description;

    @Schema(description = "The rental's surface", example = "50.0")
    private double surface;

    @Schema(description = "The rental's picture", example = "picture.jpg")
    private MultipartFile picture;
}
