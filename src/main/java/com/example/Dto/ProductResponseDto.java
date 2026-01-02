package com.example.Dto;

import lombok.Data;

@Data
public class ProductResponseDto {

    private Long id;           // Database-generated ID
    private String name;
    private String slug;
    private String description;

    private String categoryId;
    private String brandId;

    private Boolean isActive;  // Status of the product
}

