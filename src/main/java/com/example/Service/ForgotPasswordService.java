package com.example.Service;

import com.example.Dto.ResetPasswordRequest;
import com.example.Dto.ResetTokenData;
import com.example.Model.User;
import com.example.Repository.UserRepository;
import com.example.util.EmailServiceReg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ForgotPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailServiceReg emailServiceReg;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 🔹 TEMPORARY IN-MEMORY STORAGE (token → email + expire)
    private final Map<String, ResetTokenData> resetTokenStore = new ConcurrentHashMap<>();

 // ================= STEP 1: SEND RESET LINK =================
    public void sendForgotPasswordRequest(String email) {

        // 1️⃣ Find the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found with email " + email));

        // 2️⃣ Generate a unique token and expiry time
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusHours(2);

        // 3️⃣ Store the token temporarily in memory
        resetTokenStore.put(token, new ResetTokenData(email, expiry));

        // 4️⃣ Build the reset link to send via email
        String resetLink = "http://localhost:5173/reset-password?token=" + token;


        // 5️⃣ Send email using your email service
        emailServiceReg.sendEmail(
                user.getEmail(),      // recipient
                "Reset Your Password", // subject
                resetLink             // body or link
        );
    }



    // ================= STEP 2: VALIDATE TOKEN =================
    public void validateToken(String token) {

        ResetTokenData data = resetTokenStore.get(token);

        if (data == null) {
            throw new RuntimeException("Invalid reset link");
        }

        if (data.getExpiry().isBefore(LocalDateTime.now())) {
            resetTokenStore.remove(token);
            throw new RuntimeException("Reset link has expired");
        }
    }

    // ================= STEP 3: RESET PASSWORD =================
    public void resetPassword(ResetPasswordRequest request) {

        // 1️⃣ Get token data
        ResetTokenData data = resetTokenStore.get(request.getToken());

        if (data == null) {
            throw new RuntimeException("Invalid reset link");
        }

        if (data.getExpiry().isBefore(LocalDateTime.now())) {
            resetTokenStore.remove(request.getToken());
            throw new RuntimeException("Reset link has expired");
        }

        // 2️⃣ Get user by email from token data
        String email = data.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email " + email));

        // 3️⃣ Update password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        // 4️⃣ Remove token after successful reset
        resetTokenStore.remove(request.getToken());
    }
}
