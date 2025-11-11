package com.parking.slot.service;

import com.parking.slot.model.Slot;
import com.parking.slot.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlotService {
    
    @Autowired
    private SlotRepository slotRepository;
    
    public List<Slot> getAllSlots() {
        return slotRepository.findAll();
    }
    
    public List<Slot> getAvailableSlots() {
        return slotRepository.findByStatus("AVAILABLE");
    }
    
    public Slot getSlotById(Long id) {
        return slotRepository.findById(id).orElse(null);
    }
    
    public Slot createSlot(Slot slot) {
        slot.setStatus("AVAILABLE");
        return slotRepository.save(slot);
    }
    
    public Slot updateSlot(Long id, Slot slotDetails) {
        Slot slot = slotRepository.findById(id).orElse(null);
        if (slot != null) {
            slot.setLocation(slotDetails.getLocation());
            slot.setStatus(slotDetails.getStatus());
            slot.setType(slotDetails.getType());
            slot.setSlotNumber(slotDetails.getSlotNumber());
            return slotRepository.save(slot);
        }
        return null;
    }
    
    public boolean deleteSlot(Long id) {
        if (slotRepository.existsById(id)) {
            slotRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public Slot updateSlotStatus(Long id, String status) {
        Slot slot = slotRepository.findById(id).orElse(null);
        if (slot != null) {
            slot.setStatus(status);
            return slotRepository.save(slot);
        }
        return null;
    }
}