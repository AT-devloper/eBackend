package com.example.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Model.SubscriptionPayment;

public interface SubscriptionPaymentRepo extends JpaRepository<SubscriptionPayment, Long> {
    Optional<SubscriptionPayment> findByRazorpaySubscriptionId(String subscriptionId);
}
