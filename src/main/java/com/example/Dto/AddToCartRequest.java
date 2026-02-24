package com.example.Dto;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Long productId;
    private Long variantId;
    private Integer quantity;
}

