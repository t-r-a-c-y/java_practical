package com.Tracy.tesiHotelBookingManagementSystem.service;

import com.Tracy.tesiHotelBookingManagementSystem.dto.BookingDTO;
import com.Tracy.tesiHotelBookingManagementSystem.entity.*;
import com.Tracy.tesiHotelBookingManagementSystem.repository.BookingRepository;
import com.Tracy.tesiHotelBookingManagementSystem.repository.RoomRepository;
import com.Tracy.tesiHotelBookingManagementSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private BookingService bookingService;

    private User user;
    private Room room;
    private BookingDTO bookingDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "John Doe", "john@example.com", "password", Role.CUSTOMER);
        room = new Room(1L, new Hotel(), "Deluxe", 100.0, true);
        bookingDTO = new BookingDTO();
        bookingDTO.setRoomId(1L);
        bookingDTO.setCheckIn(LocalDate.now().plusDays(1));
        bookingDTO.setCheckOut(LocalDate.now().plusDays(2));
    }

    @Test
    void testCreateBookingSuccess() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(bookingRepository.findByRoomIdAndStatus(1L, BookingStatus.CONFIRMED)).thenReturn(Collections.emptyList());
        when(bookingRepository.save(any(Booking.class))).thenReturn(new Booking());

        Booking booking = bookingService.createBooking(bookingDTO);

        assertNotNull(booking);
        verify(roomRepository).save(room);
        verify(emailService).sendBookingConfirmation(user.getEmail(), any(Booking.class));
    }

    @Test
    void testCreateBookingRoomNotAvailable() {
        room.setIsAvailable(false);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(bookingDTO));
    }

    @Test
    void testCancelBookingSuccess() {
        Booking booking = new Booking(1L, user, room, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), BookingStatus.CONFIRMED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.cancelBooking(1L);

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertTrue(booking.getRoom().getIsAvailable());
        verify(bookingRepository).save(booking);
        verify(roomRepository).save(room);
    }

    @Test
    void testCancelBookingPastDate() {
        Booking booking = new Booking(1L, user, room, LocalDate.now().minusDays(1), LocalDate.now(), BookingStatus.CONFIRMED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThrows(RuntimeException.class, () -> bookingService.cancelBooking(1L));
    }
}