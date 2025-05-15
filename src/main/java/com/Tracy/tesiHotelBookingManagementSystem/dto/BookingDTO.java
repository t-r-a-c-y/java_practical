package com.Tracy.tesiHotelBookingManagementSystem.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingDTO {
    private Long roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
}
