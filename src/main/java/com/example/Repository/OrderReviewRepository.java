package com.example.Repository;

import com.example.Model.OrderReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderReviewRepository extends JpaRepository<OrderReview, Long> {

    List<OrderReview> findByProductId(Long productId);

    List<OrderReview> findByUserId(Long userId);

    Optional<OrderReview> findByOrderIdAndProductId(Long orderId, Long productId);
}
