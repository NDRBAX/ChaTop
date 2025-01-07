package com.chatop.rental_portal_backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chatop.rental_portal_backend.dto.SendMessageDTO;
import com.chatop.rental_portal_backend.mappers.SendMessageMapper;
import com.chatop.rental_portal_backend.models.Message;
import com.chatop.rental_portal_backend.repositories.MessageRepository;

@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;
    private final SendMessageMapper sendMessageMapper;

    public MessageService(MessageRepository messageRepository, SendMessageMapper sendMessageMapper) {
        this.messageRepository = messageRepository;
        this.sendMessageMapper = sendMessageMapper;
    }

    public void sendMessage(SendMessageDTO message) {

        logger.info(">>> MESSAGE SENT");
        Message newMessage = sendMessageMapper.sendMessageDTOToMessage(message);

        messageRepository.save(newMessage);
    }
}
