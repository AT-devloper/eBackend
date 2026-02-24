package com.example.Dto;

import lombok.Data;
import java.util.List;

@Data
public class ProductListItemDto {
    private Long productId;
    private String name;
    private String brand;
    private Double price;
    private String image;

    // ✅ ADD THIS
    private List<VariantResponseDto> variants;
}
