package com.example.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Model.VariantPrice;

public interface VariantPriceRepository extends JpaRepository<VariantPrice, Long> {
    Optional<VariantPrice> findByVariantId(Long variantId);
}

