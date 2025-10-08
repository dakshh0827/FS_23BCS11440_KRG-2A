package com.parking.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "global_settings")
public class GlobalSettings {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "default_penalty_amount", nullable = false)
    private double defaultPenaltyAmount = 50.0;

    @Column(name = "default_hourly_rate", nullable = false)
    private double defaultHourlyRate = 10.0;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
