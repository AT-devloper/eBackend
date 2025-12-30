package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Model.User;
import com.example.Repository.UserRepository;
import com.example.util.EmailServiceReg;
import com.example.util.OTPUtil;
import com.example.util.PhoneService;
import com.example.util.OTPService;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailServiceReg emailServiceReg;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OTPService otpService; // in-memory OTP service

 
    public void registerUser(User user) {
        // Check if username exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Format phone and check if it exists
        String formattedPhone = formatPhone(user.getPhone());
        user.setPhone(formattedPhone);
        if (userRepository.findByPhone(formattedPhone).isPresent()) {
            throw new RuntimeException("Phone number already registered");
        }

        // Encode password and set status
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus("PENDING");

        // Generate OTPs
        String emailOtp = OTPUtil.generateOTP();
        String phoneOtp = OTPUtil.generateOTP();

        // Store OTPs directly with 15 minutes expiry
        otpService.storeOtp("email_otp:" + user.getEmail(), emailOtp, 15);
        otpService.storeOtp("phone_otp:" + formattedPhone, phoneOtp, 15);

        // Send OTPs
        emailServiceReg.sendEmail(user.getEmail(), "Verify Your Email OTP", "Your OTP: " + emailOtp);
        phoneService.sendOTP(formattedPhone, phoneOtp);

        // Save user
        userRepository.save(user);
    }




    // Verify phone OTP
    public boolean verifyPhoneOTP(String phone, String otp) {
        String formattedPhone = formatPhone(phone);
        String key = "phone_otp:" + formattedPhone;

        boolean valid = otpService.validateOtp(key, otp);

        if (valid) {
            User user = userRepository.findByPhone(formattedPhone)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setPhoneVerified(true);
            activateIfReady(user);
            userRepository.save(user);
        }

        return valid;
    }

    // Verify email OTP
    public boolean verifyEmailOTP(String email, String otp) {
        String key = "email_otp:" + email;

        boolean valid = otpService.validateOtp(key, otp);

        if (valid) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setEmailVerified(true);
            activateIfReady(user);
            userRepository.save(user);
        }

        return valid;
    }

    // Activate user if both email and phone verified
    private void activateIfReady(User user) {
        if (user.isEmailVerified() && user.isPhoneVerified()) {
            user.setStatus("ACTIVE");
        }
    }

    // Ensure phone number starts with country code +91 if missing
    private String formatPhone(String phone) {
        return phone.startsWith("+") ? phone : "+91" + phone;
    }
}
