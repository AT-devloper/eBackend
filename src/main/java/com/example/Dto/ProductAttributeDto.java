package com.example.Dto;

import lombok.Data;
import java.util.List;

@Data
public class ProductAttributeDto {
    private Long attributeId;
    private List<Long> valueIds;   // multiple values
}

