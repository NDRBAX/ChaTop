package com.chatop.rental_portal_backend.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CreateRentalDTO {
    private String name;
    private double price;
    private String description;
    private double surface;
    private MultipartFile picture;
}
