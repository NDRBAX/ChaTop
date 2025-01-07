package com.chatop.rental_portal_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatop.rental_portal_backend.models.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
