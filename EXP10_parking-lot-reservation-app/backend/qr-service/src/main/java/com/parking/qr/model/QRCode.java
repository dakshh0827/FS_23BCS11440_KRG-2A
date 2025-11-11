package com.parking.qr.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "qr_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QRCode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qrId;
    
    @Column(nullable = false)
    private Long bookingId;
    
    @Column(nullable = false, unique = true)
    private String code;
    
    @Column(nullable = false)
    private LocalDateTime validity;
    
    @Column(nullable = false)
    private Boolean isUsed = false;
}