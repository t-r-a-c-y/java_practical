package com.Tracy.tesiHotelBookingManagementSystem.dto;

import lombok.Data;

@Data
public class RoomDTO {
    private Long hotelId;
    private String roomType;
    private Double price;
    private Boolean isAvailable;
}