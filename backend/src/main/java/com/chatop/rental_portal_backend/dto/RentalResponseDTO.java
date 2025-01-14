package com.chatop.rental_portal_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RentalResponseDTO {
    @Schema(description = "The rental's ID", example = "1")
    private int id;

    @Schema(description = "The rental's name", example = "Cozy apartment")
    private String name;

    @Schema(description = "The rental's surface", example = "50.0")
    private double surface;

    @Schema(description = "The rental's price", example = "1000.0")
    private double price;

    @Schema(description = "The rental's picture", example = "picture.jpg")
    private String picture;

    @Schema(description = "The rental's description", example = "A cozy apartment in the heart of the city")
    private String description;

    @Schema(description = "The rental's owner ID", example = "1")
    @JsonProperty("owner_id")
    private int ownerId;

    @Schema(description = "The rental's creation date", example = "2025-01-01 12:00:00")
    @JsonProperty("created_at")
    private String createdAt;

    @Schema(description = "The rental's last update date", example = "2025-01-01 12:00:00")
    @JsonProperty("updated_at")
    private String updatedAt;
}
