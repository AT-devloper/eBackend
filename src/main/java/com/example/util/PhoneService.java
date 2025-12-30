package com.example.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;

@Service
public class PhoneService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;

    public void sendOTP(String toPhoneNumber, String otp) {

        Twilio.init(accountSid, authToken);

        Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(twilioPhoneNumber),
                "Your OTP is " + otp
        ).create();
    }
    
    @PostConstruct
    public void checkTwilioConfig() {
        System.out.println("Twilio SID: " + accountSid);
        System.out.println("Twilio Phone: " + twilioPhoneNumber);
    }

}
