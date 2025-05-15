package com.Tracy.tesiHotelBookingManagementSystem.service;

import com.Tracy.tesiHotelBookingManagementSystem.entity.Booking;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBookingConfirmation(String to, Booking booking) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Booking Confirmation");
        message.setText("Your booking for room " + booking.getRoom().getRoomType() +
                " from " + booking.getCheckIn() + " to " + booking.getCheckOut() +
                " has been confirmed. Booking ID: " + booking.getId());
        mailSender.send(message);
    }
}
