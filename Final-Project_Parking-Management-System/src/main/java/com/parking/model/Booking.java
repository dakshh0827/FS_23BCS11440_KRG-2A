package com.parking.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "slot_id", nullable = false)
    private UUID slotId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.ACTIVE;

    @Column(nullable = false)
    private boolean penalty = false;

    @Column(name = "penalty_amount", nullable = false)
    private double penaltyAmount = 0.0;

    @Column(name = "booking_amount", nullable = false)
    private double bookingAmount = 0.0;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount = 0.0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum BookingStatus {
        ACTIVE,
        COMPLETED,
        CANCELLED
    }
}
