package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Model.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProductIdAndVariantIdIsNull(Long productId);

    List<ProductImage> findByProductIdAndVariantId(Long productId, Long variantId);

    List<ProductImage> findAllByProductIdInAndIsPrimaryTrue(List<Long> productIds);

    // New batch method for fallback images
    List<ProductImage> findByProductIdInAndVariantIdIsNull(List<Long> productIds);

    List<ProductImage> findByProductIdAndIsPrimaryTrue(Long productId);

}
