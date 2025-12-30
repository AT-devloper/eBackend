package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Dto.LoginRegister;
import com.example.Dto.RegisterRequest;
import com.example.Model.User;
import com.example.Repository.UserRepository;
import com.example.Security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // REGISTER USER
    public String register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            return "Already registered";
        }

        User user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .username(req.getUsername())
                .phone(req.getPhone())
                .build();

        userRepository.save(user);
        return "Registered Successfully";
    }

    // LOGIN WITH EMAIL OR PHONE
    public String login(LoginRegister req) {
        User user = null;

        if (req.getEmail() != null && !req.getEmail().isEmpty()) {
            user = userRepository.findByEmail(req.getEmail())
                    .orElseThrow(() -> new RuntimeException("Invalid Email or Phone"));
        } else if (req.getPhone() != null && !req.getPhone().isEmpty()) {
            user = userRepository.findByPhone(req.getPhone())
                    .orElseThrow(() -> new RuntimeException("Invalid Email or Phone"));
        } else {
            throw new RuntimeException("Email or Phone is required");
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}
