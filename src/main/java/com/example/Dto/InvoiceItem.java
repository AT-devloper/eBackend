package com.example.Dto;

import lombok.Data;

@Data
public class InvoiceItem {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;

}

