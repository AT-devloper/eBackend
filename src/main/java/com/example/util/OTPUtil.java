package com.example.util;

import java.util.Random;

public class OTPUtil {
	
	public static String generateOTP() {
		
		Random random = new Random();
		int number = 100000 + random.nextInt(900000); // for 6 digit OTP
		return String.valueOf(number);
	}
}
