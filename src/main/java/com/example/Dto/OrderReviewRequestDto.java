package com.example.Dto;

import lombok.Data;

@Data
//Ensure your DTO looks like this
public class OrderReviewRequestDto {
 private Long productId;
 private Long orderId;
 private Long userId;
 private Integer rating;
 private String comment;
}
