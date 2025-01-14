package com.chatop.rental_portal_backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.chatop.rental_portal_backend.dto.RegisterRequestDTO;
import com.chatop.rental_portal_backend.models.User;

@Mapper(componentModel = "spring")
public interface RegisterUserMapper {
    RegisterUserMapper INSTANCE = Mappers.getMapper(RegisterUserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    User registerRequestDTOToUser(RegisterRequestDTO registerRequestDTO);
}
