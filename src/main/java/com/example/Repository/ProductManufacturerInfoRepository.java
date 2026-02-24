package com.example.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Model.ProductManufacturerInfo;

@Repository
public interface ProductManufacturerInfoRepository extends JpaRepository<ProductManufacturerInfo, Long> {
    Optional<ProductManufacturerInfo> findByProductId(Long productId);
    void deleteByProductId(Long productId);
}
