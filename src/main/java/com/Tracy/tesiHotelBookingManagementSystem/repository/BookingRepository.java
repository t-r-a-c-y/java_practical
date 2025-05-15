package com.Tracy.tesiHotelBookingManagementSystem.repository;

import com.Tracy.tesiHotelBookingManagementSystem.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import com.Tracy.tesiHotelBookingManagementSystem.entity.BookingStatus;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByRoomIdAndStatus(Long roomId, BookingStatus status);
}
