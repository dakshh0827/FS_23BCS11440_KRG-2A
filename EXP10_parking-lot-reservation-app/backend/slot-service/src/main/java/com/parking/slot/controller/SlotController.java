package com.parking.slot.controller;

import com.parking.slot.model.Slot;
import com.parking.slot.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slots")
// @CrossOrigin(origins = "http://localhost:5173")
public class SlotController {
    
    @Autowired
    private SlotService slotService;
    
    @GetMapping
    public ResponseEntity<List<Slot>> getAllSlots() {
        return ResponseEntity.ok(slotService.getAllSlots());
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<Slot>> getAvailableSlots() {
        return ResponseEntity.ok(slotService.getAvailableSlots());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Slot> getSlotById(@PathVariable Long id) {
        Slot slot = slotService.getSlotById(id);
        if (slot != null) {
            return ResponseEntity.ok(slot);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<Slot> createSlot(@RequestBody Slot slot) {
        return ResponseEntity.ok(slotService.createSlot(slot));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Slot> updateSlot(@PathVariable Long id, @RequestBody Slot slot) {
        Slot updated = slotService.updateSlot(id, slot);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Slot> updateSlotStatus(@PathVariable Long id, @RequestBody String status) {
        Slot updated = slotService.updateSlotStatus(id, status);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long id) {
        if (slotService.deleteSlot(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}