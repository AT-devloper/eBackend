package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Model.ProductFeature;

@Repository
public interface ProductFeatureRepository extends JpaRepository<ProductFeature, Long> {

    // Delete all features for a product
    void deleteByProductId(Long productId);

    // Get all features for a product
    List<ProductFeature> findByProductId(Long productId);
}

