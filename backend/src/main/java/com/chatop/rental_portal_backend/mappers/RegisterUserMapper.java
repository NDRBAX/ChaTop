package com.chatop.rental_portal_backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.chatop.rental_portal_backend.dto.UserRegisterDTO;
import com.chatop.rental_portal_backend.models.User;

@Mapper(componentModel = "spring")
public interface RegisterUserMapper {
    RegisterUserMapper INSTANCE = Mappers.getMapper(RegisterUserMapper.class);

    User userRegisterDTOToUser(UserRegisterDTO userRegisterDTO);

}
