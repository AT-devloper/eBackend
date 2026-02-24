package com.example.Dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductResponseDto {
    private Long productId; // ✅ map from Product.id
    private String name;
    private String description;
    private Long categoryId;
    private Long brandId;
    private Boolean isActive;
    private List<String> images;
}
