package com.parking.booking.services;

import com.parking.booking.client.QRClient;
import com.parking.booking.client.SlotClient;
import com.parking.booking.model.Booking;
import com.parking.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private SlotClient slotClient;
    
    @Autowired
    private QRClient qrClient;
    
    public Map<String, Object> createBooking(Long userId, Long slotId, LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Create booking
            Booking booking = new Booking();
            booking.setUserId(userId);
            booking.setSlotId(slotId);
            booking.setStartTime(startTime);
            booking.setEndTime(endTime);
            booking.setStatus("ACTIVE");
            
            Booking savedBooking = bookingRepository.save(booking);
            
            // Update slot status
            slotClient.updateSlotStatus(slotId, "RESERVED");
            
            // Generate QR code
            Map<String, Object> qrRequest = new HashMap<>();
            qrRequest.put("bookingId", savedBooking.getBookingId());
            Map<String, Object> qrResponse = qrClient.generateQR(qrRequest);
            
            savedBooking.setQrCode((String) qrResponse.get("qrCode"));
            bookingRepository.save(savedBooking);
            
            response.put("success", true);
            response.put("booking", savedBooking);
            response.put("message", "Booking created successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to create booking: " + e.getMessage());
        }
        
        return response;
    }
    
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
    
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }
    
    public Map<String, Object> cancelBooking(Long bookingId) {
        Map<String, Object> response = new HashMap<>();
        
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null) {
            booking.setStatus("CANCELLED");
            bookingRepository.save(booking);
            
            // Update slot status back to available
            slotClient.updateSlotStatus(booking.getSlotId(), "AVAILABLE");
            
            response.put("success", true);
            response.put("message", "Booking cancelled successfully");
        } else {
            response.put("success", false);
            response.put("message", "Booking not found");
        }
        
        return response;
    }
    
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}