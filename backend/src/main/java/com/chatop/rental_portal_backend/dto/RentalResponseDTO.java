package com.chatop.rental_portal_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RentalResponseDTO {
    private int id;
    private String name;
    private double surface;
    private double price;
    private String picture;
    private String description;

    @JsonProperty("owner_id")
    private int ownerId;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;
}
