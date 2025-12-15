package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Dto.RegisterRequest;
import com.example.Model.UserModel;
import com.example.Repository.UserRepository;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class AuthService {

	@Autowired
    UserRepository userRepository;
	@Autowired
    PasswordEncoder passwordEncoder;
	
	public String register(RegisterRequest req) {
		
//      if (userRepository.existsByEmail(req.getEmail())) {
//      return "Already registered";
//  }
		
		UserModel userModel = UserModel.builder()
						 	.email(req.getEmail())
			                .password(passwordEncoder.encode(req.getPassword()))
			                .username(req.getUsername())
			                .phone(req.getPhone())
							.build();
		  userRepository.save(userModel);
		return "Registered Successfully";	
	}
	

}
