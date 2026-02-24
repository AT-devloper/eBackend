package com.example.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Model.OrderPayment;

public interface OrderPaymentRepo extends JpaRepository<OrderPayment, Long> {
    Optional<OrderPayment> findByRazorpayOrderId(String orderId);
}
