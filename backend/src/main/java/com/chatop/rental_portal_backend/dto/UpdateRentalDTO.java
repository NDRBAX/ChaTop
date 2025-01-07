package com.chatop.rental_portal_backend.dto;

import lombok.Data;

@Data
public class UpdateRentalDTO {
    private String name;
    private double price;
    private String description;
    private double surface;
}
