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
import com.chatop.rental_portal_backend.services.impl.IRentalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rentals", description = "Endpoints for rental management. There are endpoints for fetching all rentals, fetching a rental by ID, creating a rental, and updating a rental.")
public class RentalController {

    private final IRentalService rentalService;

    public RentalController(IRentalService rentalService) {
        this.rentalService = rentalService;
    }


    @Operation(
        summary = "Fetch all rentals",
        description = "Fetches all rentals from the database.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User details retrieved successfully", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = RentalsResponseDTO.class))

            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Void.class),
                examples = @ExampleObject(value = "",
                summary = "Unauthorized")
                )
            )
        }
    )
    @SecurityRequirement(name = "bearer-key")
    @GetMapping
    public ResponseEntity<RentalsResponseDTO> getRentals() {
        log.info(">>> GETTING ALL RENTALS <<<");
        List<RentalResponseDTO> rentals = rentalService.getRentals();
        RentalsResponseDTO response = new RentalsResponseDTO(rentals);
        return ResponseEntity.ok(response);
    }


    @Operation(
        summary = "Fetch rental by ID",
        description = "Fetches a rental by its ID from the database.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = RentalResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Void.class),
                examples = @ExampleObject(value = "",
                summary = "Unauthorized")
                )
            )
        }
    )
    @Parameter(
        name = "id",
        description = "The rental ID",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = int.class))
    )
    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/{id}")
    public ResponseEntity<RentalResponseDTO> getRentalById(@Valid @PathVariable int id) {
        log.info(">>> GETTING RENTAL BY ID >>> {} <<<", id);
        RentalResponseDTO rental = rentalService.getRentalById(id);
        return ResponseEntity.ok(rental);
    }

    @Operation(
        summary = "Create a rental",
        description = "Creates a rental in the database.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Rental created successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResponseMessageDTO.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "message": "Rental created !"
                    }
                    """,
                    summary = "Rental created successfully")
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Void.class),
                examples = @ExampleObject(value = "",
                summary = "Unauthorized"))
            )
        }
    )
    @Parameter(
        name = "createRentalDTO",
        description = "The rental creation request",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = CreateRentalDTO.class))
    )
    @SecurityRequirement(name = "bearer-key")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ResponseMessageDTO> createRental(
            @Valid @ModelAttribute CreateRentalDTO createRentalDTO) throws IOException {
        log.info(">>> CREATING A RENTAL >>> {} <<<", createRentalDTO);
        rentalService.createRental(createRentalDTO);
        return ResponseEntity.ok(new ResponseMessageDTO("Rental created !"));
    }

    @Operation(
        summary = "Update a rental",
        description = "Updates a rental in the database.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Rental updated successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResponseMessageDTO.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "message": "Rental updated !"
                    }
                    """,
                    summary = "Rental updated successfully")
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Void.class),
                examples = @ExampleObject(value = "",
                summary = "Unauthorized"))
            )
        }
    )
    @Parameter(
        name = "id",
        description = "The rental ID",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = int.class))
    )
    @Parameter(
        name = "updateRentalDTO",
        description = "The rental update request",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = UpdateRentalDTO.class))
    )
    @SecurityRequirement(name = "bearer-key")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessageDTO> updateRental(@Valid @PathVariable int id,
            @Valid @ModelAttribute UpdateRentalDTO updateRentalDTO) {
        log.info(">>> UPDATING A RENTAL BY ID >>> {} <<<", id);
        rentalService.updateRental(id, updateRentalDTO);
        return ResponseEntity.ok(new ResponseMessageDTO("Rental updated !"));
    }

}
