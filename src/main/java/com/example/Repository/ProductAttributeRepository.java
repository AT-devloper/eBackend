package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Model.ProductAttribute;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {

    // Fetch all attributes linked to a product
    List<ProductAttribute> findByProductId(Long productId);

    // Delete all attributes for a given product
    void deleteByProductId(Long productId);
}
