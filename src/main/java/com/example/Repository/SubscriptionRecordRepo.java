package com.example.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Model.SubscriptionRecord;

public interface SubscriptionRecordRepo extends JpaRepository<SubscriptionRecord, Long> {
    Optional<SubscriptionRecord> findBySubscriptionId(String subId);
}
