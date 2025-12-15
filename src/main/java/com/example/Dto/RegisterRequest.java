package com.example.Dto;

import lombok.Data;

@Data
public class RegisterRequest {

	public String email;
	public String password;
	public String username;
	public String phone;
}
