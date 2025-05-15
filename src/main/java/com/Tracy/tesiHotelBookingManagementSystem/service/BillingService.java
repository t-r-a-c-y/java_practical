package com.Tracy.tesiHotelBookingManagementSystem.service;

import com.Tracy.tesiHotelBookingManagementSystem.entity.Billing;
import com.Tracy.tesiHotelBookingManagementSystem.repository.BillingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingService {

    private final BillingRepository billingRepository;

    public BillingService(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    public Billing getBillingByBookingId(Long bookingId) {
        return billingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Billing not found"));
    }

    public List<Billing> getAllBillings() {
        return billingRepository.findAll();
    }
}
