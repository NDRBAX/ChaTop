package com.chatop.rental_portal_backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.chatop.rental_portal_backend.dto.CreateRentalDTO;
import com.chatop.rental_portal_backend.models.Rental;

@Mapper(componentModel = "spring")
public interface CreateRentalMapper {
    CreateRentalMapper INSTANCE = Mappers.getMapper(CreateRentalMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "picture", ignore = true)
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    Rental toRental(CreateRentalDTO createRentalDTO);
}
