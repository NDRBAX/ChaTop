package com.chatop.rental_portal_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.rental_portal_backend.dto.MessageRequestDTO;
import com.chatop.rental_portal_backend.dto.ResponseMessageDTO;
import com.chatop.rental_portal_backend.services.IMessageService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final IMessageService messageService;

    public MessageController(IMessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<ResponseMessageDTO> sendMessage(@Valid @RequestBody MessageRequestDTO messageRequest) {
        log.info("Received message: {}", messageRequest);
        messageService.sendMessage(messageRequest);
        return ResponseEntity.ok(new ResponseMessageDTO("Message sent with success"));
    }
}
