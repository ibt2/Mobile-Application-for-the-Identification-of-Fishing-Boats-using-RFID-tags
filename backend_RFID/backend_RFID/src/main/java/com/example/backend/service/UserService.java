package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(String username, String rawPassword) {
        logger.debug("Encoding password for user: {}", username);
        String encodedPassword = passwordEncoder.encode(rawPassword);
        logger.info("Attempting to save new user: {}", username);
        User savedUser = userRepository.save(new User(username, encodedPassword));
        logger.info("User saved successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    public User getUserByUsername(String username) {
        logger.debug("Attempting to find user by username: {}", username);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            logger.debug("User found: {}", username);
        } else {
            logger.debug("User not found with username: {}", username);
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Loading user details for authentication for username: {}", username);
        User user = userRepository.findByUsername(username);

        if (user == null) {
            logger.warn("Authentication failed: User not found with username: {}", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        logger.info("User details loaded for authentication: {}", user.getUsername());
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    public String generateResetPasswordToken(String username) {
        logger.info("Received request to generate reset password token for username: {}", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.warn("Failed to generate reset token: User not found for username: {}", username);
            return null;
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(15);

        user.setResetPasswordToken(token);
        user.setResetPasswordExpiresAt(expiryDate);
        userRepository.save(user);

        logger.info("Successfully generated reset token for user {}. Token expires at: {}", username, expiryDate);
        // Do NOT log the actual token here if this were a production system, for security.
        return token;
    }

    public boolean resetPassword(String token, String newPassword) {
        logger.info("Attempting to reset password using token.");
        logger.debug("Searching for user with reset token: {}", token != null ? token.substring(0, Math.min(token.length(), 8)) + "..." : "null"); // Log partial token
        User user = userRepository.findByResetPasswordToken(token);

        if (user == null) {
            logger.warn("Password reset failed: Invalid or non-existent token provided.");
            return false;
        }

        logger.debug("User found for reset token. Checking token expiry for user: {}", user.getUsername());
        if (user.getResetPasswordExpiresAt() == null || user.getResetPasswordExpiresAt().isBefore(LocalDateTime.now())) {
            logger.warn("Password reset failed for user {}: Provided token has expired.", user.getUsername());
            user.setResetPasswordToken(null);
            user.setResetPasswordExpiresAt(null);
            userRepository.save(user); // Invalidate the token in DB
            return false;
        }

        // Token is valid, proceed with password reset
        logger.debug("Token is valid for user {}. Encoding new password.", user.getUsername());
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpiresAt(null);
        userRepository.save(user);

        logger.info("Password successfully reset for user: {}", user.getUsername());
        return true;
    }
}