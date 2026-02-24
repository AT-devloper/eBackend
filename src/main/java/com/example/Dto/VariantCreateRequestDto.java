package com.example.Dto;

import java.util.Map;

import lombok.Data;

@Data
public class VariantCreateRequestDto {
	
	private String sku;
	private Double price;
	private Integer stock;
	
	// AttributeId -> AttributeValueId
	private Map<Long, Long> attributes;
}
