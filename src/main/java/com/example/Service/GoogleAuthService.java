package com.example.Service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.Model.User;
import com.example.Repository.UserRepository;
import com.example.Security.JwtUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@Service
public class GoogleAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${google.client-id}")
    private String googleClientId;

    public String loginWithGoogle(String idTokenString) {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (Exception e) {
            throw new RuntimeException("Invalid Google token");
        }

        if (idToken == null) {
            throw new RuntimeException("Invalid Google token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        boolean emailVerified = Boolean.TRUE.equals(payload.getEmailVerified());

        if (!emailVerified) {
            throw new RuntimeException("Email not verified by Google");
        }

        // Try to find existing user by email
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    // Generate unique username based on email
                    String baseUsername = email.split("@")[0];
                    String username = baseUsername;
                    int count = 1;
                    while(userRepository.existsByUsername(username)) {
                        username = baseUsername + count;
                        count++;
                    }

                    User newUser = User.builder()
                            .email(email)
                            .password("GOOGLE_LOGIN")
                            .username(username)
                            .phone("") // optional: remove if you want blank
                            .build();
                    return userRepository.save(newUser);
                });

        return jwtUtil.generateToken(user.getEmail());
    }

}
