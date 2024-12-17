package com.chatop.rental_portal_backend.mappers;

import com.chatop.rental_portal_backend.dto.AuthenticatedUserDTO;
import com.chatop.rental_portal_backend.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthenticatedUserMapper {
    AuthenticatedUserDTO userToAuthenticatedUserDTO(User user);
}
