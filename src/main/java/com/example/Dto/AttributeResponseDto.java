package com.example.Dto;

import lombok.Data;
import java.util.List;

@Data
public class AttributeResponseDto {
    private Long id;
    private String name;
    private List<String> values;            // All possible values
    private List<String> assignedValues;    // Assigned values (can be multiple)
}
