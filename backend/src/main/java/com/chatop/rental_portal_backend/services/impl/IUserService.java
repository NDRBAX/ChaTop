package com.chatop.rental_portal_backend.services.impl;

import com.chatop.rental_portal_backend.dto.AuthenticatedUserDTO;

public interface IUserService {
    AuthenticatedUserDTO getUserById(int id);
}
