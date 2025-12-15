package com.example.Controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Dto.LoginRegister;
import com.example.Dto.RegisterRequest;
import com.example.Service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	@Autowired
	AuthService authService;
	
	@PostMapping("/register")
	public String register(@RequestBody RegisterRequest req) {
		return authService.register(req);
	}
	
	@PostMapping("/login")
	public String login(@RequestBody LoginRegister req) {
		return authService.login(req);
	}

}
