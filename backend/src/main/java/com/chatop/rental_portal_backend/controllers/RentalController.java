package com.chatop.rental_portal_backend.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.rental_portal_backend.dto.CreateRentalDTO;
import com.chatop.rental_portal_backend.dto.RentalResponseDTO;
import com.chatop.rental_portal_backend.dto.UpdateRentalDTO;
import com.chatop.rental_portal_backend.services.RentalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private static final Logger logger = LoggerFactory.getLogger(RentalController.class);

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    // Get all rentals -> 200 / 401
    @GetMapping
    public ResponseEntity<Map<String, List<RentalResponseDTO>>> getRentals() {
        logger.info(">>> GETTING ALL RENTALS <<<");
        List<RentalResponseDTO> rentals = rentalService.getRentals();
        return ResponseEntity.ok(Map.of("rentals", rentals));
    }

    // Get a rental by id -> 200 / 401
    @GetMapping("/{id}")
    public ResponseEntity<RentalResponseDTO> getRentalById(@Valid @PathVariable int id) {
        logger.info(">>> GETTING RENTAL BY ID >>> {} <<<", id);
        RentalResponseDTO rental = rentalService.getRentalById(id);
        return ResponseEntity.ok(rental);
    }

    // Create a rental -> 200 / 401
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> createRental(
            @Valid @ModelAttribute CreateRentalDTO createRentalDTO) throws IOException {
        logger.info(">>> CREATING A RENTAL >>> {} <<<", createRentalDTO);
        rentalService.createRental(createRentalDTO);
        return ResponseEntity.ok(Map.of("message", "Rental created !"));
    }

    // Update a rental -> 200 / 401
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateRental(@Valid @PathVariable int id,
            @Valid @ModelAttribute UpdateRentalDTO udateRentalDTO) {
        logger.info(">>> UPDATING A RENTAL BY ID >>> {} <<<", id);
        rentalService.updateRental(id, udateRentalDTO);
        return ResponseEntity.ok(Map.of("message", "Rental updated !"));
    }

}
