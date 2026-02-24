package com.example.Repository;

import com.example.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Order, Long> {

    // Fetch all orders of a user (used for invoice creation)
    List<Order> findByUserId(Long userId);
}
