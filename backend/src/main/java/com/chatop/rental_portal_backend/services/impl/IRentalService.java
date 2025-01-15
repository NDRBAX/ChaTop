package com.chatop.rental_portal_backend.services.impl;

import java.util.List;

import com.chatop.rental_portal_backend.dto.CreateRentalDTO;
import com.chatop.rental_portal_backend.dto.RentalResponseDTO;
import com.chatop.rental_portal_backend.dto.UpdateRentalDTO;

public interface IRentalService {
    List<RentalResponseDTO> getRentals();

    RentalResponseDTO getRentalById(int id);

    void createRental(CreateRentalDTO createRentalDTO);

    void updateRental(int id, UpdateRentalDTO updateRentalDTO);
}
