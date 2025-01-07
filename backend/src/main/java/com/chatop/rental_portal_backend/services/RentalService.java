package com.chatop.rental_portal_backend.services;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chatop.rental_portal_backend.dto.CreateRentalDTO;
import com.chatop.rental_portal_backend.dto.RentalResponseDTO;
import com.chatop.rental_portal_backend.dto.UpdateRentalDTO;
import com.chatop.rental_portal_backend.mappers.AuthenticatedUserMapper;
import com.chatop.rental_portal_backend.mappers.CreateRentalMapper;
import com.chatop.rental_portal_backend.models.Rental;
import com.chatop.rental_portal_backend.models.User;
import com.chatop.rental_portal_backend.repositories.RentalRepository;

@Service
public class RentalService {
    private static final Logger logger = LoggerFactory.getLogger(RentalService.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final RentalRepository rentalRepository;
    private final CloudinaryService cloudinaryService;
    private final AuthService authService;
    private final AuthenticatedUserMapper authenticatedUserMapper;
    private final CreateRentalMapper createRentalMapper;

    public RentalService(RentalRepository rentalRepository, CloudinaryService cloudinaryService,
            AuthService authService, AuthenticatedUserMapper authenticatedUserMapper,
            CreateRentalMapper createRentalMapper) {
        this.rentalRepository = rentalRepository;
        this.cloudinaryService = cloudinaryService;
        this.authService = authService;
        this.authenticatedUserMapper = authenticatedUserMapper;
        this.createRentalMapper = createRentalMapper;

    }

    // Get all rentals
    public List<RentalResponseDTO> getRentals() {
        logger.info(">>> GETTING ALL RENTALS");
        List<Rental> rentalsFromDb = rentalRepository.findAll();

        List<RentalResponseDTO> rentals = rentalsFromDb.stream()
                .map(rental -> new RentalResponseDTO(
                        rental.getId(),
                        rental.getName(),
                        rental.getSurface(),
                        rental.getPrice(),
                        rental.getPicture(),
                        rental.getDescription(),
                        rental.getUser().getId(),
                        dateFormat.format(rental.getCreated_at()),
                        dateFormat.format(rental.getUpdated_at())))
                .collect(Collectors.toList());

        return rentals;
    }

    // Get a rental by id
    public RentalResponseDTO getRentalById(int id) {
        logger.info(">>> GETTING RENTAL BY ID");
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        return new RentalResponseDTO(
                rental.getId(),
                rental.getName(),
                rental.getSurface(),
                rental.getPrice(),
                rental.getPicture(),
                rental.getDescription(),
                rental.getUser().getId(),
                dateFormat.format(rental.getCreated_at()),
                dateFormat.format(rental.getUpdated_at()));
    }

    // Create a rental
    public void createRental(CreateRentalDTO createRentalDTO) {
        Optional<String> imageUrl = cloudinaryService.uploadImage(createRentalDTO.getPicture());

        if (imageUrl.isEmpty()) {
            logger.error("### CREATE RENTAL ### Error uploading image");
            throw new RuntimeException("Error uploading image");
        }

        User authenticatedUser = authenticatedUserMapper.toUser(authService.getAuthenticatedUser());
        Rental rental = createRentalMapper.toRental(createRentalDTO);
        rental.setPicture(imageUrl.get());
        rental.setUser(authenticatedUser);
        rentalRepository.save(rental);
    }

    // Update a rental by id
    public void updateRental(int id, UpdateRentalDTO updateRentalDTO) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        rental.setName(updateRentalDTO.getName());
        rental.setSurface(updateRentalDTO.getSurface());
        rental.setPrice(updateRentalDTO.getPrice());
        rental.setDescription(updateRentalDTO.getDescription());
        rentalRepository.save(rental);
    }

}
