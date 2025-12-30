package com.example.Controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Model.User;
import com.example.Service.RegistrationService;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    // 1️⃣ Register User
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        registrationService.registerUser(user);
        return ResponseEntity.ok("User registered. OTPs sent to email and phone.");
    }

    // 2️⃣ Verify Phone OTP
    @PostMapping("/verify/phone")
    public ResponseEntity<String> verifyPhoneOTP(
            @RequestParam String phone,
            @RequestParam String otp) {

        boolean verified = registrationService.verifyPhoneOTP(phone, otp);

        return verified
                ? ResponseEntity.ok("Phone verified successfully.")
                : ResponseEntity.badRequest().body("Invalid or expired phone OTP.");
    }

    // 3️⃣ Verify Email OTP ✅ FIXED
    @PostMapping("/verify/email")
    public ResponseEntity<String> verifyEmailOTP(
            @RequestParam String email,
            @RequestParam String otp) {

        boolean verified = registrationService.verifyEmailOTP(email, otp);

        return verified
                ? ResponseEntity.ok("Email verified successfully.")
                : ResponseEntity.badRequest().body("Invalid or expired email OTP.");
    }
}
