package com.chatop.rental_portal_backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chatop.rental_portal_backend.dto.MessageRequestDTO;
import com.chatop.rental_portal_backend.models.Message;
import com.chatop.rental_portal_backend.models.Rental;
import com.chatop.rental_portal_backend.models.User;
import com.chatop.rental_portal_backend.repositories.RentalRepository;
import com.chatop.rental_portal_backend.repositories.UserRepository;

@Mapper(componentModel = "spring", uses = { SendMessageMapper.Helper.class })
public interface SendMessageMapper {
    SendMessageMapper INSTANCE = Mappers.getMapper(SendMessageMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    @Mapping(target = "rental", source = "rental_id")
    @Mapping(target = "user", source = "user_id")
    Message messageRequestDTOToMessage(MessageRequestDTO messageRequest);

    @Component
    class Helper {
        @Autowired
        private RentalRepository rentalRepository;

        @Autowired
        private UserRepository userRepository;

        public Rental mapRental(int rental_id) {
            return rentalRepository.findById(rental_id).orElse(null);
        }

        public User mapUser(int user_id) {
            return userRepository.findById(user_id).orElse(null);
        }
    }
}
