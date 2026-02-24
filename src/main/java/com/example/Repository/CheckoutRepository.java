package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.Model.Checkout;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

    // Fetch checkout snapshot
    List<Checkout> findByUserId(Long userId);

    // Clear checkout after order success
    @Transactional
    void deleteByUserId(Long userId);
}
