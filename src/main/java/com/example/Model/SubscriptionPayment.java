package com.example.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "subscription_payment")
public class SubscriptionPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(name = "razorpay_subscription_id")
    private String razorpaySubscriptionId;

    private String status; // PENDING / ACTIVE / HALTED / CANCELLED

    private LocalDateTime lastPaidAt;
}
