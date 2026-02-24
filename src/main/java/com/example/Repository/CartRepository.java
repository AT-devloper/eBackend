package com.example.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.Model.CartItem;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    // Fetch user cart
    List<CartItem> findByUserId(Long userId);

    // Find existing cart item (add/update logic)
    Optional<CartItem> findByUserIdAndProductIdAndVariantId(
        Long userId,
        Long productId,
        Long variantId
    );

    // Clear cart AFTER order success
    @Transactional
    void deleteByUserId(Long userId);
}
