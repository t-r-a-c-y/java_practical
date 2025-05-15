package com.Tracy.tesiHotelBookingManagementSystem.repository;

import com.Tracy.tesiHotelBookingManagementSystem.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelId(Long hotelId);
}
