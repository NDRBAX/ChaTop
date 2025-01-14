package com.chatop.rental_portal_backend.services;

import com.chatop.rental_portal_backend.dto.MessageRequestDTO;

public interface IMessageService {
    void sendMessage(MessageRequestDTO messageRequest);
}
