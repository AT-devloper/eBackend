package com.example.Model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "orders") // ✅ never use "order"
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long paymentId; // FK -> OrderPayment.id

    private String orderNumber;

    private String orderStatus;   // CREATED, CONFIRMED, SHIPPED

    private String paymentStatus; // DUE, PAID

    @Column(columnDefinition = "json")
    private String itemsJson; 
    
    // Add these fields
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
