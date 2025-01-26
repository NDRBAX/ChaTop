package com.chatop.rental_portal_backend.services.implementations;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chatop.rental_portal_backend.models.User;
import com.chatop.rental_portal_backend.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDetailsLoaderService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsLoaderService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by their email.
     *
     * This method loads a user from the repository by their email and returns a
     * UserDetails object representing the user.
     *
     * @param email the email of the user to load.
     * @return a UserDetails object representing the user.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("### Attempting to load user by email {} ###", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }
}
