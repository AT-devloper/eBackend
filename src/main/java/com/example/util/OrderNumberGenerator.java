package com.example.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class OrderNumberGenerator {

    private static final Random RANDOM = new Random();

    public static String generate() {
        // Format: ORD-YYYYMMDD-HHMMSS-XXXX (random 4 digits)
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        int randomDigits = 1000 + RANDOM.nextInt(9000); // 1000–9999
        return "ORD-" + timestamp + "-" + randomDigits;
    }
}
