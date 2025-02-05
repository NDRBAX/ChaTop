package com.chatop.rental_portal_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.rental_portal_backend.dto.MessageRequestDTO;
import com.chatop.rental_portal_backend.dto.ResponseMessageDTO;
import com.chatop.rental_portal_backend.services.IMessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages", description = "Endpoints for message management")
public class MessageController {

    private final IMessageService messageService;

    public MessageController(IMessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(summary = "Send a message", description = "Sends a message to a user.", responses = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessageDTO.class), examples = @ExampleObject(value = """
                    {
                        "message": "Message sent with success"
                    }
                    """, summary = "Message sent successfully"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class), examples = @ExampleObject(value = "", summary = "Unauthorized")))
    })
    @Parameter(name = "messageRequest", description = "The message to be sent", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageRequestDTO.class)))
    @SecurityRequirement(name = "bearer-key")
    @PostMapping
    public ResponseEntity<ResponseMessageDTO> sendMessage(@Valid @RequestBody MessageRequestDTO messageRequest) {
        log.info("Received message: {}", messageRequest);
        messageService.sendMessage(messageRequest);
        return ResponseEntity.ok(new ResponseMessageDTO("Message sent with success"));
    }
}
