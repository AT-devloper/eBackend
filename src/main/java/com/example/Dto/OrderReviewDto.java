package com.example.Dto;

import lombok.Data;
import java.time.Instant; // use Instant type

@Data
public class OrderReviewDto {

    private Long id;
    private Long userId;
    private Long orderId;
    private Long productId;
    private int rating;
    private String comment;
    private Instant createdAt; // directly store Instant
}
