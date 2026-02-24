package com.example.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Entity
@Data
@Table(name = "order_reviews")
public class OrderReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long orderId;
    private Long productId;

    private int rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private Instant createdAt; // simple timestamp
}
