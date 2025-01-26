package com.chatop.rental_portal_backend.services.implementations;

import org.springframework.stereotype.Service;

import com.chatop.rental_portal_backend.dto.MessageRequestDTO;
import com.chatop.rental_portal_backend.mappers.SendMessageMapper;
import com.chatop.rental_portal_backend.models.Message;
import com.chatop.rental_portal_backend.repositories.MessageRepository;
import com.chatop.rental_portal_backend.services.IMessageService;

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

    /**
     * Sends a message.
     *
     * This method saves the message to the database.
     *
     * @param messageRequest the message to send.
     */
    @Override
    public void sendMessage(MessageRequestDTO messageRequest) {
        log.info(">>> MESSAGE SENT");
        Message newMessage = sendMessageMapper.messageRequestDTOToMessage(messageRequest);
        messageRepository.save(newMessage);
    }
}
