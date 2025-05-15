package com.Tracy.tesiHotelBookingManagementSystem.repository;

import com.Tracy.tesiHotelBookingManagementSystem.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillingRepository extends JpaRepository<Billing, Long> {
    Optional<Billing> findByBookingId(Long bookingId);
}
