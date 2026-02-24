package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Model.VariantAttributeValue;

@Repository
public interface VariantAttributeValueRepository extends JpaRepository<VariantAttributeValue, Long> {

    List<VariantAttributeValue> findByVariantId(Long variantId);

    // ✅ Add this batch query
    List<VariantAttributeValue> findByVariantIdIn(List<Long> variantIds);
}
