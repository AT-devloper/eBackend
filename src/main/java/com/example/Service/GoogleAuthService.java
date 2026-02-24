package com.example.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.Model.Role;
import com.example.Model.User;
import com.example.Model.UserRole;
import com.example.Repository.RoleRepository;
import com.example.Repository.UserRepository;
import com.example.Repository.UserRoleRepository;
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
    private UserRoleRepository userRoleRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${google.client-id}")
    private String googleClientId;

    public Map<String, Object> loginWithGoogle(String idTokenString) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance()
        ).setAudience(Collections.singletonList(googleClientId))
         .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) throw new RuntimeException("Invalid Google token");

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            boolean emailVerified = Boolean.TRUE.equals(payload.getEmailVerified());
            if (!emailVerified) throw new RuntimeException("Email not verified");

            // Find or create Google user
            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> userRepository.save(
                        User.builder()
                            .email(email)
                            .username(payload.get("name") != null ? payload.get("name").toString() : email)
                            .password("GOOGLE_LOGIN")
                            .emailVerified(true)
                            .status("ACTIVE")
                            .build()
                    ));

            String token = jwtUtil.generateToken(user);

            // Fetch roles for the user
            List<UserRole> userRoles = userRoleRepo.findByUserId(user.getId());
            List<String> roles = new ArrayList<>();
            for (UserRole ur : userRoles) {
                Role role = roleRepo.findById(ur.getRoleId()).orElseThrow();
                roles.add(role.getName());
            }

            // Return user info with roles
            return Map.of(
                "token", token,
                "user", Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "username", user.getUsername(),
                    "phone", user.getPhone() != null ? user.getPhone() : "",
                    "status", user.getStatus(),
                    "roles", roles
                )
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Google login failed: " + e.getMessage());
        }
    }
}