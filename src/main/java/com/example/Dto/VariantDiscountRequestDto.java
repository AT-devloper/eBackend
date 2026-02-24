package com.example.Dto;

import com.example.Model.DiscountType;

import lombok.Data;

@Data
public class VariantDiscountRequestDto {
	
	private DiscountType discountType;
	private Double discountValue;
}
