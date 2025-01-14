package com.chatop.rental_portal_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageRequestDTO {
    @Schema(description = "The message to be sent", example = "Hello, I am interested in your rental")
    private String message;

    @Schema(description = "The rental ID", example = "1")
    private int rental_id;

    @Schema(description = "The user ID", example = "1")
    private int user_id;
}
