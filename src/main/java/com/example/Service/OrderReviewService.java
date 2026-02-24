package com.example.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Dto.OrderReviewRequestDto;
import com.example.Dto.OrderReviewDto;
import com.example.Model.Order;
import com.example.Model.OrderReview;
import com.example.Repository.OrderReviewRepository;
import com.example.Repository.OrderTrackingRepository;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderReviewService {

    private final OrderReviewRepository reviewRepo;
    private final OrderTrackingRepository orderRepo;

    @Transactional
    public OrderReviewDto createReview(OrderReviewRequestDto request) {

        Order order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!"DELIVERED".equalsIgnoreCase(order.getOrderStatus())) {
            throw new RuntimeException("Only delivered orders can be reviewed");
        }

        // Fix: Use longValue() to ensure primitive comparison
        if (order.getUserId().longValue() != request.getUserId().longValue()) {
            throw new RuntimeException("Unauthorized review attempt. Order belongs to User ID: " + order.getUserId());
        }
        OrderReview review = reviewRepo
                .findByOrderIdAndProductId(request.getOrderId(), request.getProductId())
                .orElse(null);

        if (review == null) {
            // ✅ CREATE
            review = new OrderReview();
            review.setUserId(request.getUserId());
            review.setOrderId(request.getOrderId());
            review.setProductId(request.getProductId());
        }

        // ✅ UPDATE (or set values for new)
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(Instant.now());

        return mapToDto(reviewRepo.save(review));
    }


    // ---------------- UPDATE REVIEW ----------------
    @Transactional
    public OrderReviewDto updateReview(Long reviewId, OrderReviewRequestDto request) {
        OrderReview review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Update with same logic style
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        
        return mapToDto(reviewRepo.save(review));
    }

    // ---------------- DELETE REVIEW ----------------
    @Transactional
    public void deleteReview(Long reviewId) {
        if (!reviewRepo.existsById(reviewId)) {
            throw new RuntimeException("Review not found");
        }
        reviewRepo.deleteById(reviewId);
    }

    public List<OrderReviewDto> getReviewsByProduct(Long productId) {
        return reviewRepo.findByProductId(productId).stream()
                .map(this::mapToDto)
                .toList();
    }

    private OrderReviewDto mapToDto(OrderReview review) {
        OrderReviewDto dto = new OrderReviewDto();
        dto.setId(review.getId());
        dto.setUserId(review.getUserId());
        dto.setOrderId(review.getOrderId());
        dto.setProductId(review.getProductId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt()); 
        return dto;
    }
}