package com.Tracy.tesiHotelBookingManagementSystem.repository;

import com.Tracy.tesiHotelBookingManagementSystem.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
