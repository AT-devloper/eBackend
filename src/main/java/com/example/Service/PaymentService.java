package com.example.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Model.OrderPayment;
import com.example.Model.SubscriptionPayment;
import com.example.Repository.OrderPaymentRepo;
import com.example.Repository.SubscriptionPaymentRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderPaymentRepo orderPaymentRepo;
    private final SubscriptionPaymentRepo subscriptionPaymentRepo;
    private final CheckoutService checkoutService;
    private final OrderService orderService;

    // --- Order Payments ---
    
    @Transactional
    public void orderCreated(Long userId, String razorpayOrderId) {
        OrderPayment payment = new OrderPayment();
        payment.setUserId(userId);
        payment.setRazorpayOrderId(razorpayOrderId);
        payment.setStatus("CREATED"); // initial status
        orderPaymentRepo.save(payment);
        System.out.println("✅ Order created in DB: " + razorpayOrderId);
    }

    @Transactional
    public void orderPaid(String razorpayOrderId, String razorpayPaymentId) {
        Optional<OrderPayment> optional = orderPaymentRepo.findByRazorpayOrderId(razorpayOrderId);

        if (optional.isEmpty()) {
            System.out.println("❌ Order not found: " + razorpayOrderId);
            return;
        }

        OrderPayment payment = optional.get();
        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setStatus("PAID");
        payment.setPaidAt(LocalDateTime.now());
        orderPaymentRepo.save(payment);

        System.out.println("✅ Order updated as PAID: " + razorpayOrderId);

        // --- NEW: Automatically create order and clear checkout/cart ---
        Long userId = payment.getUserId();
        orderService.createOrderFromCheckout(userId, payment.getId());
    }

    // --- Subscription Payments ---
    @Transactional
    public void subscriptionCreated(Long userId, String subscriptionId) {
        SubscriptionPayment sub = new SubscriptionPayment();
        sub.setUserId(userId);
        sub.setRazorpaySubscriptionId(subscriptionId);
        sub.setStatus("PENDING");
        subscriptionPaymentRepo.save(sub);
        System.out.println("✅ Subscription created: " + subscriptionId);
    }

    @Transactional
    public void subscriptionPaid(String subscriptionId) {
        Optional<SubscriptionPayment> optional = subscriptionPaymentRepo.findByRazorpaySubscriptionId(subscriptionId);

        if (optional.isEmpty()) {
            System.out.println("❌ Subscription not found: " + subscriptionId);
            return;
        }

        SubscriptionPayment sub = optional.get();
        sub.setStatus("ACTIVE");
        sub.setLastPaidAt(LocalDateTime.now());
        subscriptionPaymentRepo.save(sub);

        System.out.println("✅ Subscription marked as PAID: " + subscriptionId);
    }

    @Transactional
    public void subscriptionHalted(String subscriptionId) {
        Optional<SubscriptionPayment> optional = subscriptionPaymentRepo.findByRazorpaySubscriptionId(subscriptionId);
        optional.ifPresent(sub -> {
            sub.setStatus("HALTED");
            subscriptionPaymentRepo.save(sub);
            System.out.println("⚠️ Subscription halted: " + subscriptionId);
        });
    }

    // --- Helpers ---
    public OrderPayment findOrder(String orderId) {
        return orderPaymentRepo.findByRazorpayOrderId(orderId).orElse(null);
    }

    public SubscriptionPayment findSubscription(String subscriptionId) {
        return subscriptionPaymentRepo.findByRazorpaySubscriptionId(subscriptionId).orElse(null);
    }
}
