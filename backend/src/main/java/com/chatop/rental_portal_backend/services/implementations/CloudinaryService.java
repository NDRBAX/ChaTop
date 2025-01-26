package com.chatop.rental_portal_backend.services.implementations;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chatop.rental_portal_backend.services.ICloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService implements ICloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Uploads an image to Cloudinary.
     *
     * This method uploads the provided image file to Cloudinary and returns the URL
     * of the uploaded image.
     *
     * @param file the image file to upload.
     * @return the URL of the uploaded image.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Optional<String> uploadImage(MultipartFile file) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "chatop"));

            return Optional.of(uploadResult.get("url").toString());
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
