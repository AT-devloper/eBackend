package com.example.Dto;


import java.util.List;

import lombok.Data;

@Data
public class AttributeRequestDto {
    private String name;
    private List<String> values;
}
