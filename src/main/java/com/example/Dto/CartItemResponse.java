package com.example.Dto;

import lombok.Data;

@Data
public class CartItemResponse {

    private Long cartItemId;

    private Long productId;

    private Long variantId;   // ✅ REQUIRED

    private String productName;

    private String image;

    private Double price;     // price per unit

    private Integer quantity;

    private Double totalPrice; // price * quantity
}
