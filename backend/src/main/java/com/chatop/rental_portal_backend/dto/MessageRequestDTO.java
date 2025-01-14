package com.chatop.rental_portal_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageRequestDTO {
    private String message;
    private int rental_id;
    private int user_id;
}
