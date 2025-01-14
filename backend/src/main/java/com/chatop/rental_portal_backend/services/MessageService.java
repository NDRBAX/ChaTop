package com.chatop.rental_portal_backend.services;

import org.springframework.stereotype.Service;

import com.chatop.rental_portal_backend.dto.MessageRequestDTO;
import com.chatop.rental_portal_backend.mappers.SendMessageMapper;
import com.chatop.rental_portal_backend.models.Message;
import com.chatop.rental_portal_backend.repositories.MessageRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageService implements IMessageService {

    private final MessageRepository messageRepository;
    private final SendMessageMapper sendMessageMapper;

    public MessageService(MessageRepository messageRepository, SendMessageMapper sendMessageMapper) {
        this.messageRepository = messageRepository;
        this.sendMessageMapper = sendMessageMapper;
    }

    @Override
    public void sendMessage(MessageRequestDTO messageRequest) {
        log.info(">>> MESSAGE SENT");
        Message newMessage = sendMessageMapper.messageRequestDTOToMessage(messageRequest);
        messageRepository.save(newMessage);
    }
}
