package com.chatop.rental_portal_backend.services;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface ICloudinaryService {
    Optional<String> uploadImage(MultipartFile file);
}
