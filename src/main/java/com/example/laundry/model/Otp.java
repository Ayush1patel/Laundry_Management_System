package com.example.laundry.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

/**
 * Simple OTP model used for pickup verification.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Otp {
    private String otpValue;
    private LocalDateTime expiresAt;
    private boolean used = false;
    private int attempts = 0;

    public Otp() {
    }

    public Otp(String otpValue, LocalDateTime expiresAt) {
        this.otpValue = otpValue;
        this.expiresAt = expiresAt;
    }

    public String getOtpValue() {
        return otpValue;
    }

    public void setOtpValue(String otpValue) {
        this.otpValue = otpValue;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    @JsonIgnore
    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }
}