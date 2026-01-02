package com.example.Dto;

import lombok.Data;

@Data
public class BrandResponseDto {

    private Long id;        // Database-generated ID
    private String name;
    private String slug;
    private String logoUrl;
}

