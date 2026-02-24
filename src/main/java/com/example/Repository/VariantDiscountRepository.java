package com.example.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Model.VariantDiscount;

public interface VariantDiscountRepository extends JpaRepository<VariantDiscount, Long>{
	Optional<VariantDiscount> findByVariantIdAndIsActiveTrue(Long variantId);
}
