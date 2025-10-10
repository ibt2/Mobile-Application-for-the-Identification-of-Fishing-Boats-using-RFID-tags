package com.example.backend.controller;

import com.example.backend.dto.AuthRequest;
import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.ResetPasswordRequest;
import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthRequest request) {
        logger.info("Received login request for username: {}", request.getUsername());
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            logger.debug("Authentication successful for user: {}", request.getUsername());

            User user = userService.getUserByUsername(request.getUsername());
            logger.debug("User details fetched for: {}", request.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername()
            ));
            logger.info("Login successful for user: {}", request.getUsername());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user {}: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        } catch (Exception e) {
            logger.error("An unexpected error occurred during login for user {}: {}", request.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Login failed: An unexpected error occurred."));
        }
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest request) {
        logger.info("Received registration request for username: {}", request.getUsername());
        try {
            if (userService.getUserByUsername(request.getUsername()) != null) {
                logger.warn("Registration failed: Username already exists: {}", request.getUsername());
                return new AuthResponse("Registration failed: Username already exists.");
            }
            userService.saveUser(request.getUsername(), request.getPassword());
            logger.info("Registration successful for user: {}", request.getUsername());
            return new AuthResponse("Registration successful for user: " + request.getUsername());
        } catch (Exception e) {
            logger.error("Registration failed for user {}: {}", request.getUsername(), e.getMessage(), e);
            return new AuthResponse("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<AuthResponse> forgotPassword(@RequestBody AuthRequest request) {
        logger.info("Received forgot password request for username: {}", request.getUsername());
        String token = userService.generateResetPasswordToken(request.getUsername());

        if (token != null) {
            logger.info("Password reset token generated for user: {}", request.getUsername());
            return ResponseEntity.ok(new AuthResponse("Password reset token generated. Please use it to reset your password.", token));
        } else {
            logger.warn("Forgot password request: User not found for username: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("User not found."));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        logger.info("Received reset password request with token.");
        logger.debug("Attempting to reset password using token: {}", request.getToken() != null ? request.getToken().substring(0, Math.min(request.getToken().length(), 8)) + "..." : "null"); // Log partial token for security
        boolean success = userService.resetPassword(request.getToken(), request.getNewPassword());

        if (success) {
            logger.info("Password reset successful.");
            return ResponseEntity.ok(new AuthResponse("Password has been reset successfully."));
        } else {
            logger.warn("Password reset failed for provided token.");
            return ResponseEntity.badRequest().body(new AuthResponse("Password reset failed: Invalid or expired token, or other issue."));
        }
    }


}