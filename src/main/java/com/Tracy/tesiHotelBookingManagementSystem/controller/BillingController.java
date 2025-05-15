package com.Tracy.tesiHotelBookingManagementSystem.controller;

import com.Tracy.tesiHotelBookingManagementSystem.entity.Billing;
import com.Tracy.tesiHotelBookingManagementSystem.service.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billings")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping("/{bookingId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<Billing> getBillingByBookingId(@PathVariable Long bookingId) {
        return ResponseEntity.ok(billingService.getBillingByBookingId(bookingId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Billing>> getAllBillings() {
        return ResponseEntity.ok(billingService.getAllBillings());
    }
}
