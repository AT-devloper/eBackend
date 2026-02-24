package com.example.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageResponseDto {

    private Long id;
    private Long productId;
    private Long variantId;
    private String imageUrl;
    private Boolean isPrimary;
    private Integer displayOrder;
}
