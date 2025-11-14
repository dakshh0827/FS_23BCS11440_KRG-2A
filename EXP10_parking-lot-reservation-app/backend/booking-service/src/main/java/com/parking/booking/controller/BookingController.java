package com.parking.booking.controller;

import com.parking.booking.model.Booking;
// This import (with 'services') is from our previous fix
import com.parking.booking.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// --- NEW IMPORT ---
import java.time.ZonedDateTime;
// ---

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
// (We correctly removed @CrossOrigin in the previous step)
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    @PostMapping("/reserve")
    public ResponseEntity<Map<String, Object>> createBooking(@RequestBody Map<String, Object> request) {
        Long userId = ((Number) request.get("userId")).longValue();
        Long slotId = ((Number) request.get("slotId")).longValue();

        // --- THIS IS THE FIX ---
        // 1. Parse the full string (including 'Z') using ZonedDateTime
        ZonedDateTime zdtStart = ZonedDateTime.parse((String) request.get("startTime"));
        ZonedDateTime zdtEnd = ZonedDateTime.parse((String) request.get("endTime"));

        // 2. Convert it to the LocalDateTime your service needs
        // This effectively strips the timezone, which is the expected behavior.
        LocalDateTime startTime = zdtStart.toLocalDateTime();
        LocalDateTime endTime = zdtEnd.toLocalDateTime();
        
        Map<String, Object> response = bookingService.createBooking(userId, slotId, startTime, endTime);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getBookingsByUserId(userId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        if (booking != null) {
            return ResponseEntity.ok(booking);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/cancel")
    public ResponseEntity<Map<String, Object>> cancelBooking(@RequestBody Map<String, Object> request) {
        Long bookingId = ((Number) request.get("bookingId")).longValue();
        Map<String, Object> response = bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }
}
