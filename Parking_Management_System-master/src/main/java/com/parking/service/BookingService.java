package com.parking.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parking.model.Booking;
import com.parking.model.Booking.BookingStatus;
import com.parking.model.ParkingSlot;
import com.parking.repository.BookingRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private ParkingSlotService parkingSlotService;
    
    @Autowired
    private GlobalSettingsService globalSettingsService;
    
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    public List<Booking> getBookingsByUserId(UUID userId) {
        return bookingRepository.findByUserId(userId);
    }
    
    public List<Booking> getActiveBookingsByUserId(UUID userId) {
        return bookingRepository.findByUserIdAndStatus(userId, BookingStatus.ACTIVE);
    }
    
    public Optional<Booking> getBookingById(UUID id) {
        return bookingRepository.findById(id);
    }
    
    public Booking createBooking(Booking booking) {
        // Set timestamps
        LocalDateTime now = LocalDateTime.now();
        booking.setCreatedAt(now);
        booking.setUpdatedAt(now);
        booking.setStatus(BookingStatus.ACTIVE);
        
        // Book the parking slot
        ParkingSlot parkingSlot = parkingSlotService.bookParkingSlot(
            booking.getSlotId(),
            booking.getUserId(),
            booking.getStartTime(),
            booking.getEndTime()
        );
        
        // Calculate booking amount
        Duration duration = Duration.between(booking.getStartTime(), booking.getEndTime());
        long hours = duration.toHours();
        if (duration.toMinutesPart() > 0) hours++;
        double bookingAmount = hours * parkingSlot.getHourlyRate();
        booking.setBookingAmount(bookingAmount);
        booking.setTotalAmount(bookingAmount);
        
        return bookingRepository.save(booking);
    }
    
    public Booking completeBooking(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        
        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new RuntimeException("Booking is not active");
        }
        
        ParkingSlot parkingSlot = parkingSlotService.getParkingSlotById(booking.getSlotId())
                .orElseThrow(() -> new RuntimeException("Parking slot not found with id: " + booking.getSlotId()));
        
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(booking.getEndTime())) {
            booking.setPenalty(true);
            Duration exceeded = Duration.between(booking.getEndTime(), now);
            long exceededHours = exceeded.toHours();
            if (exceeded.toMinutesPart() > 0) exceededHours++;
            
            double penaltyAmount = globalSettingsService.getGlobalSettings().getDefaultPenaltyAmount() 
                    + (exceededHours * parkingSlot.getHourlyRate());
            booking.setPenaltyAmount(penaltyAmount);
            booking.setTotalAmount(booking.getBookingAmount() + penaltyAmount);
        } else {
            booking.setTotalAmount(booking.getBookingAmount());
        }
        
        parkingSlotService.releaseParkingSlot(booking.getSlotId());
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setUpdatedAt(now);
        
        return bookingRepository.save(booking);
    }
    
    public Booking cancelBooking(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        
        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new RuntimeException("Booking is not active");
        }
        
        parkingSlotService.releaseParkingSlot(booking.getSlotId());
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(LocalDateTime.now());
        
        return bookingRepository.save(booking);
    }
    
    public void deleteBooking(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        
        if (booking.getStatus() == BookingStatus.ACTIVE) {
            parkingSlotService.releaseParkingSlot(booking.getSlotId());
        }
        
        bookingRepository.delete(booking);
    }
    
    public List<Booking> getBookingsWithPenalty() {
        return bookingRepository.findAll().stream()
                .filter(Booking::isPenalty)
                .collect(Collectors.toList());
    }
}
