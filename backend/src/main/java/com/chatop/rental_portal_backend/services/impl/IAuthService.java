package com.chatop.rental_portal_backend.services.impl;

import com.chatop.rental_portal_backend.dto.AuthenticatedUserDTO;
import com.chatop.rental_portal_backend.dto.LoginRequestDTO;
import com.chatop.rental_portal_backend.dto.RegisterRequestDTO;

public interface IAuthService {
    String registerUser(RegisterRequestDTO registerRequestDTO);

    String loginUser(LoginRequestDTO userLoginDTO);

    AuthenticatedUserDTO getAuthenticatedUser();

}
