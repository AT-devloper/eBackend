package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Dto.LoginRegister;
import com.example.Dto.RegisterRequest;
import com.example.Model.UserModel;
import com.example.Repository.UserRepository;
import com.example.Security.JwtUtil;

@Service
public class AuthService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    JwtUtil jwtUtil;

    public String register(RegisterRequest req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            return "Already registered";
        }

        UserModel userModel = UserModel.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .username(req.getUsername())
                .phone(req.getPhone())
                .build();

        userRepository.save(userModel);
        return "Registered Successfully";
    }

    public String login(LoginRegister req) {

    	
        UserModel userModel = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Email"));

        if (!passwordEncoder.matches(req.getPassword(), userModel.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        return jwtUtil.generateToken(userModel.getEmail());
    }
}
