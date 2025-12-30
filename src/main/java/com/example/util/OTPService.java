package com.example.util;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OTPService {

    private static class OtpDetails {
        private final String otp;
        private final LocalDateTime expiryTime;

        public OtpDetails(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() { return otp; }
        public LocalDateTime getExpiryTime() { return expiryTime; }
    }

    private final Map<String, OtpDetails> otpCache = new ConcurrentHashMap<>();

    // Store OTP with expiry in minutes
    public void storeOtp(String key, String otp, int expiryMinutes) {
        otpCache.put(key, new OtpDetails(otp, LocalDateTime.now().plusMinutes(expiryMinutes)));
    }

    // Validate OTP; returns true if correct and removes it
    public boolean validateOtp(String key, String otp) {
        OtpDetails details = otpCache.get(key);
        if (details == null) return false;

        if (LocalDateTime.now().isAfter(details.getExpiryTime())) {
            otpCache.remove(key);
            return false;
        }

        boolean valid = details.getOtp().equals(otp);
        if (valid) otpCache.remove(key); // Remove after successful validation
        return valid;
    }

    // Delete OTP manually if needed
    public void deleteOtp(String key) {
        otpCache.remove(key);
    }
}
