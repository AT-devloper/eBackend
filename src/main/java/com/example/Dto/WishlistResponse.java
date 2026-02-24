package com.example.Dto;

import lombok.Data;

@Data
public class WishlistResponse {
    private Long wishlistId;
    private Long productId;
    private String productName;
    private String image;
    private Double price;
    private String category;
}
