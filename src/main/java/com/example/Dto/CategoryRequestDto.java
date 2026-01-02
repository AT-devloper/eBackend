package com.example.Dto;

import lombok.Data;

@Data
public class CategoryRequestDto {

	private String tempId;        // temporary unique ID for mapping
    private String parentTempId;  // reference to parent's tempId
    private String name;
    private String slug;
	
	
}
