package com.Tracy.tesiHotelBookingManagementSystem.service;

import com.Tracy.tesiHotelBookingManagementSystem.dto.RoomDTO;
import com.Tracy.tesiHotelBookingManagementSystem.entity.Hotel;
import com.Tracy.tesiHotelBookingManagementSystem.entity.Room;
import com.Tracy.tesiHotelBookingManagementSystem.repository.HotelRepository;
import com.Tracy.tesiHotelBookingManagementSystem.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    public RoomService(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

    public Room createRoom(RoomDTO roomDTO) {
        Hotel hotel = hotelRepository.findById(roomDTO.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomType(roomDTO.getRoomType());
        room.setPrice(roomDTO.getPrice());
        room.setIsAvailable(roomDTO.getIsAvailable());
        return roomRepository.save(room);
    }

    public List<Room> getRoomsByHotelId(Long hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }
}
