package com.chatop.rental_portal_backend.mappers;

import org.mapstruct.Mapper;

import com.chatop.rental_portal_backend.dto.AuthenticatedUserDTO;
import com.chatop.rental_portal_backend.models.User;

@Mapper(componentModel = "spring")
public interface AuthenticatedUserMapper {
    AuthenticatedUserDTO userToAuthenticatedUserDTO(User user);
}
