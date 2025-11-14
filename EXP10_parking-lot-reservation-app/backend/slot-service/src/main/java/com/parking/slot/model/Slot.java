package com.parking.slot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "slots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Slot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slotId;
    
    @Column(nullable = false)
    private String location;
    
    @Column(nullable = false)
    private String status; // AVAILABLE, OCCUPIED, RESERVED
    
    @Column(nullable = false)
    private String type; // REGULAR, VIP, HANDICAPPED
    
    @Column(nullable = false)
    private String slotNumber;
}