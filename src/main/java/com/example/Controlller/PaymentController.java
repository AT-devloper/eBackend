package com.example.Controlller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.example.Service.PaymentService;
import com.example.Service.CheckoutService;
import com.example.Service.OrderService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final CheckoutService checkoutService;
    private final OrderService orderService;

    @Value("${razorpay.key_id}")
    private String KEY;

    @Value("${razorpay.key_secret}")
    private String SECRET;

    @Value("${razorpay.plan_id}")
    private String PLAN_ID;  // ✅ Use plan ID from application.properties

    /** ----------------- ONE-TIME ORDER ----------------- */
    @PostMapping("/create-order")
    public Map<String, String> createOrder(@RequestBody Map<String, Object> data) throws Exception {
        Long userId = Long.valueOf(data.get("userId").toString());

        // 1️⃣ Create checkout from cart
        var checkoutItems = checkoutService.createCheckout(userId);
        if (checkoutItems.isEmpty())
            throw new RuntimeException("Cart is empty, cannot create order");

        // 2️⃣ Calculate total amount (sum of checkout items)
        int amount = checkoutItems.stream()
                .mapToInt(i -> (int) (i.getTotalPrice() * 100)) // convert to paise
                .sum();

        // 3️⃣ Create Razorpay order
        RazorpayClient client = new RazorpayClient(KEY, SECRET);
        JSONObject req = new JSONObject();
        req.put("amount", amount);
        req.put("currency", "INR");
        req.put("payment_capture", 1);

        Order order = client.orders.create(req);

        // 4️⃣ Save order in DB
        paymentService.orderCreated(userId, order.get("id"));

        return Map.of(
                "orderId", order.get("id"),
                "key", KEY,
                "amount", String.valueOf(amount / 100) // send back in ₹
        );
    }

    /** ----------------- SUBSCRIPTION ----------------- */
    @PostMapping("/create-subscription")
    public Map<String, String> createSubscription(@RequestBody Map<String, Object> data) throws Exception {
        Long userId = Long.valueOf(data.get("userId").toString());

        // 1️⃣ Create Razorpay subscription
        RazorpayClient client = new RazorpayClient(KEY, SECRET);
        JSONObject req = new JSONObject();
        req.put("plan_id", PLAN_ID); // ✅ Use injected plan ID
        req.put("customer_notify", 1);
        req.put("total_count", 12); // 12 months
        req.put("start_at", System.currentTimeMillis() / 1000);

        var subscription = client.subscriptions.create(req);

        // 2️⃣ Save subscription in DB
        paymentService.subscriptionCreated(userId, subscription.get("id"));

        return Map.of(
                "subscriptionId", subscription.get("id"),
                "key", KEY
        );
    }

    /** ----------------- CHECKOUT ITEMS ----------------- */
    @GetMapping("/checkout/{userId}")
    public Map<String, Object> getCheckout(@PathVariable Long userId) {
        var items = checkoutService.getCheckoutByUser(userId);
        double total = items.stream().mapToDouble(i -> i.getTotalPrice()).sum();
        return Map.of(
                "items", items,
                "total", total
        );
    }

    /** ----------------- STATUS CHECK (orders & subscriptions) ----------------- */
    @GetMapping("/status/check/{id}")
    public Map<String, Object> getPaymentStatus(@PathVariable String id) {

        // Check order first
        var order = paymentService.findOrder(id);
        if (order != null) {
            return Map.of(
                    "type", "order",
                    "status", order.getStatus() != null ? order.getStatus() : "DUE",
                    "razorpayPaymentId", order.getRazorpayPaymentId() != null ? order.getRazorpayPaymentId() : ""
            );
        }

        // Check subscription
        var sub = paymentService.findSubscription(id);
        if (sub != null) {
            return Map.of(
                    "type", "subscription",
                    "status", sub.getStatus() != null ? sub.getStatus() : "PENDING",
                    "lastPaidAt", sub.getLastPaidAt() != null ? sub.getLastPaidAt().toString() : null
            );
        }

        return Map.of("status", "NOT_FOUND");
    }
}
