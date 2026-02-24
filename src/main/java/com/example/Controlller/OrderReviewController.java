package com.example.Controlller;

import com.example.Dto.OrderReviewDto;
import com.example.Dto.OrderReviewRequestDto;
import com.example.Service.OrderReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/reviews")
@RequiredArgsConstructor
public class OrderReviewController {

    private final OrderReviewService reviewService;

    @PostMapping
    public OrderReviewDto createReview(@RequestBody OrderReviewRequestDto request) {
        return reviewService.createReview(request);
    }

    @GetMapping("/product/{productId}")
    public List<OrderReviewDto> getReviewsByProduct(@PathVariable Long productId) {
        return reviewService.getReviewsByProduct(productId);
    }

    // 🔥 Update Logic
    @PutMapping("/{reviewId}")
    public OrderReviewDto updateReview(@PathVariable Long reviewId, @RequestBody OrderReviewRequestDto request) {
        return reviewService.updateReview(reviewId, request);
    }

    // 🔥 Delete Logic
    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}