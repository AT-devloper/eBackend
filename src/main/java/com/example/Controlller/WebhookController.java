package com.example.Controlller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Service.PaymentService;
import com.example.Service.WebhookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webhook")
public class WebhookController {

    private final WebhookService webhookService;
    private final PaymentService paymentService;

    @Value("${razorpay.webhook_secret}")
    private String WEBHOOK_SECRET;

    @PostMapping("/razorpay")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature
    ) {
        try {
            if (!webhookService.verify(payload, signature, WEBHOOK_SECRET)) {
                System.out.println("❌ Invalid Razorpay webhook signature");
                return ResponseEntity.status(401).body("Invalid signature");
            }

            JSONObject json = new JSONObject(payload);
            String event = json.optString("event");
            JSONObject entity = null;

            if (event.startsWith("subscription")) {
                entity = json.optJSONObject("payload")
                             .optJSONObject("subscription")
                             .optJSONObject("entity");
            } else if (event.startsWith("payment")) {
                entity = json.optJSONObject("payload")
                             .optJSONObject("payment")
                             .optJSONObject("entity");
            }

            if (entity == null) {
                System.out.println("ℹ️ No entity in payload for event: " + event);
                return ResponseEntity.ok("No entity in payload");
            }

            switch (event) {
                case "payment.authorized":
                    System.out.println("ℹ️ Payment authorized: " + entity.optString("order_id"));
                    break;

                case "payment.captured":
                    paymentService.orderPaid(entity.optString("order_id"), entity.optString("id"));
                    System.out.println("🎉 Payment captured: " + entity.optString("order_id"));
                    break;

                case "subscription.activated":
                case "subscription.charged":
                    paymentService.subscriptionPaid(entity.optString("id"));
                    System.out.println("🎉 Subscription paid: " + entity.optString("id"));
                    break;

                case "subscription.halted":
                case "subscription.pending":
                case "subscription.cancelled":
                    paymentService.subscriptionHalted(entity.optString("id"));
                    System.out.println("⚠️ Subscription halted/pending/cancelled: " + entity.optString("id"));
                    break;

                default:
                    System.out.println("⚠️ Unhandled event: " + event);
            }

        } catch (Exception e) {
            System.err.println("❌ Error processing webhook: " + e.getMessage());
        }

        return ResponseEntity.ok("Webhook processed"); // Always respond 200
    }

}
