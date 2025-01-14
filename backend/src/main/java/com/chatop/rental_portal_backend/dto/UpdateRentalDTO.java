package com.chatop.rental_portal_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateRentalDTO {
    @Schema(description = "The name of the rental", example = "Apartment 1")
    private String name;

    @Schema(description = "The price of the rental", example = "1000")
    private double price;

    @Schema(description = "The description of the rental", example = "A cozy apartment in the heart of the city")
    private String description;

    @Schema(description = "The surface of the rental", example = "50")
    private double surface;
}
