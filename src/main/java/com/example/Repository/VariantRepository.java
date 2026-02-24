package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Model.Variant;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Long> {

    @Query("SELECT v.productId, MIN(v.price) FROM Variant v WHERE v.productId IN :productIds GROUP BY v.productId")
    List<Object[]> findLowestPriceByProductIds(@Param("productIds") List<Long> productIds);

    List<Variant> findByProductIdIn(List<Long> productIds);
    List<Variant> findByProductId(Long productId);
}
