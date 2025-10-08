package com.parking.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parking.model.ParkingSlot;
import com.parking.model.ParkingSlot.SlotStatus;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, UUID> {
    List<ParkingSlot> findByStatus(SlotStatus status);
    Boolean existsBySlotNumber(String slotNumber);
}
