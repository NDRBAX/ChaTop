package com.chatop.rental_portal_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.chatop.rental_portal_backend.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
