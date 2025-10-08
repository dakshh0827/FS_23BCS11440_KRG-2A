package com.parking.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parking.model.Booking;
import com.parking.model.Booking.BookingStatus;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByUserId(UUID userId);
    List<Booking> findBySlotId(UUID slotId);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByUserIdAndStatus(UUID userId, BookingStatus status);
}
