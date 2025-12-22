package com.example.Controlller; // Fixed typo in package name

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Dto.GoogleLoginRequest;
import com.example.Dto.LoginRegister;
import com.example.Dto.RegisterRequest;
import com.example.Service.AuthService;
import com.example.Service.GoogleAuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private GoogleAuthService googleAuthService; // Injected as instance

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest req) {
        return authService.register(req);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRegister req) {
        return authService.login(req);
    }

    @PostMapping("/google")
    public String googleLogin(@RequestBody GoogleLoginRequest req) {
        return googleAuthService.loginWithGoogle(req.getIdToken()); // use injected instance
    }
}
