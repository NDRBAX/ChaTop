package com.chatop.rental_portal_backend.services.implementations;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.chatop.rental_portal_backend.dto.CreateRentalDTO;
import com.chatop.rental_portal_backend.dto.RentalResponseDTO;
import com.chatop.rental_portal_backend.dto.UpdateRentalDTO;
import com.chatop.rental_portal_backend.mappers.AuthenticatedUserMapper;
import com.chatop.rental_portal_backend.mappers.CreateRentalMapper;
import com.chatop.rental_portal_backend.models.Rental;
import com.chatop.rental_portal_backend.models.User;
import com.chatop.rental_portal_backend.repositories.RentalRepository;
import com.chatop.rental_portal_backend.services.IAuthService;
import com.chatop.rental_portal_backend.services.ICloudinaryService;
import com.chatop.rental_portal_backend.services.IRentalService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RentalService implements IRentalService {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final RentalRepository rentalRepository;
    private final ICloudinaryService cloudinaryService;
    private final IAuthService authService;
    private final AuthenticatedUserMapper authenticatedUserMapper;
    private final CreateRentalMapper createRentalMapper;

    public RentalService(RentalRepository rentalRepository, ICloudinaryService cloudinaryService,
            IAuthService authService, AuthenticatedUserMapper authenticatedUserMapper,
            CreateRentalMapper createRentalMapper) {
        this.rentalRepository = rentalRepository;
        this.cloudinaryService = cloudinaryService;
        this.authService = authService;
        this.authenticatedUserMapper = authenticatedUserMapper;
        this.createRentalMapper = createRentalMapper;

    }

    /**
     * Retrieves a list of all rentals.
     *
     * This method fetches all rental entities from the repository, maps them to
     * RentalResponseDTO objects, and returns the list of these DTOs. Each DTO
     * contains the rental's id, name, surface, price, picture, description, user id,
     * creation date, and last updated date.
     *
     * @return a list of RentalResponseDTO objects representing all rentals.
     */
    @Override
    public List<RentalResponseDTO> getRentals() {
        log.info(">>> GETTING ALL RENTALS");
        return rentalRepository.findAll().stream()
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
    }

    
    /**
     * Retrieves a rental by its ID.
     *
     * @param id the ID of the rental to retrieve
     * @return a RentalResponseDTO containing the rental details
     */
    @Override
    public RentalResponseDTO getRentalById(int id) {
        log.info(">>> GETTING RENTAL BY ID");
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

    /**
     * Creates a new rental entry.
     *
     * This method uploads the rental picture to the cloud storage, maps the authenticated user
     * and rental data transfer object (DTO) to their respective entities, and saves the rental
     * information to the repository.
     *
     * @param createRentalDTO the data transfer object containing rental information
     */
    @Override
    public void createRental(CreateRentalDTO createRentalDTO) {
        Optional<String> imageUrl = cloudinaryService.uploadImage(createRentalDTO.getPicture());

        if (imageUrl.isEmpty()) {
            log.error("### CREATE RENTAL ### Error uploading image");
            throw new RuntimeException("Error uploading image");
        }

        User authenticatedUser = authenticatedUserMapper.toUser(authService.getAuthenticatedUser());

        Rental rental = createRentalMapper.toRental(createRentalDTO);
        rental.setPicture(imageUrl.get());
        rental.setUser(authenticatedUser);
        rentalRepository.save(rental);
    }

    /**
     * Updates a rental entry in the system.
     *
     * This method updates the rental information with the
     * data from the updateRentalDTO object, and then saves the updated rental information to the repository.
     *
     * @param id the ID of the rental to update
     * @param updateRentalDTO the data transfer object containing the updated rental information
     */
    @Override
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
