package com.example.Dto;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private Long userId;
    private Integer amount;
}
