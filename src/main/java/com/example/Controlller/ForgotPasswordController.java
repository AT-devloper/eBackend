package com.example.Controlller;

import com.example.Dto.ResetPasswordRequest;
import com.example.Service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @Autowired
    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    // ================= STEP 1: Send reset link =================
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        forgotPasswordService.sendForgotPasswordRequest(email);
        return ResponseEntity.ok("Reset link has been sent to your email if it exists!");
    }

    // ================= STEP 2: Validate token =================
    @GetMapping("/reset-password/validate")
    public ResponseEntity<String> validate(@RequestParam String token) {
        forgotPasswordService.validateToken(token);
        return ResponseEntity.ok("Token is valid!");
    }

    // ================= STEP 3: Reset password =================
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        forgotPasswordService.resetPassword(request);
        return ResponseEntity.ok("Password reset successful!");
    }
}
