package com.chatop.rental_portal_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatop.rental_portal_backend.models.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
}
