package com.example.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VariantListItemDto {
	private Long id;
    private String sku;
    private Integer stock;
}
