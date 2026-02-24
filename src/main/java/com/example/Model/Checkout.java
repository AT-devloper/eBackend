package com.example.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "checkout")
public class Checkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    private Double price;        // per item price
    private Double totalPrice;   // price * quantity

    private Long productId;
    private Integer quantity;
    private String status;       // CREATED / PAID
    private Long userId;
    private Long variantId;
}
