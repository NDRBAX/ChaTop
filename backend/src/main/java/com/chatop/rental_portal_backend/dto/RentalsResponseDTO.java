package com.chatop.rental_portal_backend.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RentalsResponseDTO {
    @Schema(description = "The list of rentals")
    @JsonProperty("rentals")
    private List<RentalResponseDTO> rentals;
}
