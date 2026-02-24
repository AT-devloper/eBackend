package com.example.Dto;

import java.util.Map;


import lombok.Data;

@Data
public class VariantSelectionRequestDto {
	
	// AttributeId -> AttributeValueId
	private Map<Long, Long> attributes;
}
