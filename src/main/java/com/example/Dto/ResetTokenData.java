package com.example.Dto;

import java.time.LocalDateTime;

public class ResetTokenData {

    private final String email;
    private final LocalDateTime expiry;

    public ResetTokenData(String email, LocalDateTime expiry) {
        this.email = email;
        this.expiry = expiry;
    }


	// Getter for email
    public String getEmail() {
        return email;
    }

    // Getter for expiry
    public LocalDateTime getExpiry() {
        return expiry;
    }
}
