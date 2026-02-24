package com.example.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Model.ProductSpecification;

@Repository
public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, Long> {

    // Get all specifications for a product
    List<ProductSpecification> findByProductId(Long productId);

    // Delete all specifications for a product
    void deleteByProductId(Long productId);

    // Optional: find a specific specification by productId and specKey
    Optional<ProductSpecification> findByProductIdAndSpecKey(Long productId, String specKey);
}
