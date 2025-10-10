package com.example.backend.dto;

public class AuthResponse {
    private String message;
    private String token; // NEW FIELD FOR RESET TOKEN

    public AuthResponse(String message) {
        this.message = message;
    }

    // NEW CONSTRUCTOR TO INCLUDE TOKEN
    public AuthResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    // Getter
    public String getMessage() {
        return message;
    }

    // Setter (optional, but good practice if used for request deserialization)
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter for token
    public String getToken() {
        return token;
    }

    // Setter for token
    public void setToken(String token) {
        this.token = token;
    }
}