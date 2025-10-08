package com.parking.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parking.model.ParkingSlot;
import com.parking.model.ParkingSlot.SlotStatus;
import com.parking.repository.ParkingSlotRepository;

@Service
public class ParkingSlotService {

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;
    
    @Autowired
    private GlobalSettingsService globalSettingsService;
    
    public List<ParkingSlot> getAllParkingSlots() {
        return parkingSlotRepository.findAll();
    }
    
    public List<ParkingSlot> getAvailableParkingSlots() {
        return parkingSlotRepository.findByStatus(SlotStatus.AVAILABLE);
    }
    
    public Optional<ParkingSlot> getParkingSlotById(UUID id) {
        return parkingSlotRepository.findById(id);
    }
    
    public ParkingSlot createParkingSlot(ParkingSlot parkingSlot) {
        if (parkingSlotRepository.existsBySlotNumber(parkingSlot.getSlotNumber())) {
            throw new RuntimeException("Slot number already exists");
        }
        if (parkingSlot.getHourlyRate() == 0.0) {
            parkingSlot.setHourlyRate(globalSettingsService.getGlobalSettings().getDefaultHourlyRate());
        }
        LocalDateTime now = LocalDateTime.now();
        parkingSlot.setCreatedAt(now);
        parkingSlot.setUpdatedAt(now);
        return parkingSlotRepository.save(parkingSlot);
    }
    
    public ParkingSlot updateParkingSlot(UUID id, ParkingSlot parkingSlotDetails) {
        ParkingSlot parkingSlot = parkingSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking slot not found with id: " + id));
        parkingSlot.setSlotNumber(parkingSlotDetails.getSlotNumber());
        parkingSlot.setStatus(parkingSlotDetails.getStatus());
        if (parkingSlotDetails.getHourlyRate() > 0) parkingSlot.setHourlyRate(parkingSlotDetails.getHourlyRate());
        parkingSlot.setUpdatedAt(LocalDateTime.now());
        return parkingSlotRepository.save(parkingSlot);
    }
    
    public void deleteParkingSlot(UUID id) {
        ParkingSlot parkingSlot = parkingSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking slot not found with id: " + id));
        parkingSlotRepository.delete(parkingSlot);
    }
    
    public ParkingSlot bookParkingSlot(UUID id, UUID userId, LocalDateTime startTime, LocalDateTime endTime) {
        ParkingSlot parkingSlot = parkingSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking slot not found with id: " + id));
        if (parkingSlot.getStatus() == SlotStatus.OCCUPIED) 
            throw new RuntimeException("Parking slot is already occupied");
        
        parkingSlot.setStatus(SlotStatus.OCCUPIED);
        parkingSlot.setBookedBy(userId);
        parkingSlot.setStartTime(startTime);
        parkingSlot.setEndTime(endTime);
        parkingSlot.setUpdatedAt(LocalDateTime.now());
        return parkingSlotRepository.save(parkingSlot);
    }
    
    public ParkingSlot releaseParkingSlot(UUID id) {
        ParkingSlot parkingSlot = parkingSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking slot not found with id: " + id));
        parkingSlot.setStatus(SlotStatus.AVAILABLE);
        parkingSlot.setBookedBy(null);
        parkingSlot.setStartTime(null);
        parkingSlot.setEndTime(null);
        parkingSlot.setUpdatedAt(LocalDateTime.now());
        return parkingSlotRepository.save(parkingSlot);
    }
    
    public ParkingSlot updateParkingSlotRate(UUID id, double hourlyRate) {
        ParkingSlot parkingSlot = parkingSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking slot not found with id: " + id));
        parkingSlot.setHourlyRate(hourlyRate);
        parkingSlot.setUpdatedAt(LocalDateTime.now());
        return parkingSlotRepository.save(parkingSlot);
    }
}
