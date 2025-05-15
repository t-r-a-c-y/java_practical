package com.Tracy.tesiHotelBookingManagementSystem.service;

import com.Tracy.tesiHotelBookingManagementSystem.dto.BookingDTO;
import com.Tracy.tesiHotelBookingManagementSystem.entity.Booking;
import com.Tracy.tesiHotelBookingManagementSystem.entity.BookingStatus;
import com.Tracy.tesiHotelBookingManagementSystem.entity.Room;
import com.Tracy.tesiHotelBookingManagementSystem.entity.User;
import com.Tracy.tesiHotelBookingManagementSystem.repository.BookingRepository;
import com.Tracy.tesiHotelBookingManagementSystem.repository.RoomRepository;
import com.Tracy.tesiHotelBookingManagementSystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository,
                          UserRepository userRepository, EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Transactional
    public Booking createBooking(BookingDTO bookingDTO) {
        Room room = roomRepository.findById(bookingDTO.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        if (!isRoomAvailable(room, bookingDTO.getCheckIn(), bookingDTO.getCheckOut())) {
            throw new RuntimeException("Room not available for the selected dates");
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        room.setIsAvailable(false);
        roomRepository.save(room);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckIn(bookingDTO.getCheckIn());
        booking.setCheckOut(bookingDTO.getCheckOut());
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking savedBooking = bookingRepository.save(booking);
        emailService.sendBookingConfirmation(user.getEmail(), savedBooking);
        return savedBooking;
    }

    public List<Booking> getUserBookings() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepository.findByUserId(user.getId());
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Transactional
    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (booking.getCheckIn().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot cancel past bookings");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        booking.getRoom().setIsAvailable(true);
        bookingRepository.save(booking);
        roomRepository.save(booking.getRoom());
    }

    private boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        if (!room.getIsAvailable()) return false;
        List<Booking> bookings = bookingRepository.findByRoomIdAndStatus(room.getId(), BookingStatus.CONFIRMED);
        for (Booking b : bookings) {
            if (!(checkOut.isBefore(b.getCheckIn()) || checkIn.isAfter(b.getCheckOut()))) {
                return false;
            }
        }
        return true;
    }
}