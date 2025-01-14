package com.chatop.rental_portal_backend.services;

import com.chatop.rental_portal_backend.dto.AuthenticatedUserDTO;

public interface IUserService {
    AuthenticatedUserDTO getUserById(int id);
}
