package com.chatop.rental_portal_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatop.rental_portal_backend.models.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
    Optional<Rental> findById(int id);
}
