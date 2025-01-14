package com.chatop.rental_portal_backend.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RentalsResponseDTO {
    @JsonProperty("rentals")
    private List<RentalResponseDTO> rentals;
}
