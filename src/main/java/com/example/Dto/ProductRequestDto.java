package com.example.Dto;

import lombok.Data;

@Data
public class ProductRequestDto {

	private String name;
	private String slug;
	private String description;
	
	private Long categoryId;
	private Long brandId;

	
}
