package com.chatop.rental_portal_backend.controllers;

import java.io.IOException;
import java.util.List;

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
import com.chatop.rental_portal_backend.dto.RentalsResponseDTO;
import com.chatop.rental_portal_backend.dto.ResponseMessageDTO;
import com.chatop.rental_portal_backend.dto.UpdateRentalDTO;
import com.chatop.rental_portal_backend.services.IRentalService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final IRentalService rentalService;

    public RentalController(IRentalService rentalService) {
        this.rentalService = rentalService;
    }

    // Get all rentals -> 200 / 401
    @GetMapping
    public ResponseEntity<RentalsResponseDTO> getRentals() {
        log.info(">>> GETTING ALL RENTALS <<<");
        List<RentalResponseDTO> rentals = rentalService.getRentals();
        RentalsResponseDTO response = new RentalsResponseDTO(rentals);
        return ResponseEntity.ok(response);
    }

    // Get a rental by id -> 200 / 401
    @GetMapping("/{id}")
    public ResponseEntity<RentalResponseDTO> getRentalById(@Valid @PathVariable int id) {
        log.info(">>> GETTING RENTAL BY ID >>> {} <<<", id);
        RentalResponseDTO rental = rentalService.getRentalById(id);
        return ResponseEntity.ok(rental);
    }

    // Create a rental -> 200 / 401
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ResponseMessageDTO> createRental(
            @Valid @ModelAttribute CreateRentalDTO createRentalDTO) throws IOException {
        log.info(">>> CREATING A RENTAL >>> {} <<<", createRentalDTO);
        rentalService.createRental(createRentalDTO);
        return ResponseEntity.ok(new ResponseMessageDTO("Rental created !"));
    }

    // Update a rental -> 200 / 401
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessageDTO> updateRental(@Valid @PathVariable int id,
            @Valid @ModelAttribute UpdateRentalDTO udateRentalDTO) {
        log.info(">>> UPDATING A RENTAL BY ID >>> {} <<<", id);
        rentalService.updateRental(id, udateRentalDTO);
        return ResponseEntity.ok(new ResponseMessageDTO("Rental updated !"));
    }

}
