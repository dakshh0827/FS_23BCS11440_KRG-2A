package com.parking.admin.service;

import com.parking.admin.client.BookingClient;
import com.parking.admin.client.SlotClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    
    @Autowired
    private BookingClient bookingClient;
    
    @Autowired
    private SlotClient slotClient;
    
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            List<Map<String, Object>> bookings = bookingClient.getAllBookings();
            List<Map<String, Object>> slots = slotClient.getAllSlots();
            
            long activeBookings = bookings.stream()
                .filter(b -> "ACTIVE".equals(b.get("status")))
                .count();
            
            long availableSlots = slots.stream()
                .filter(s -> "AVAILABLE".equals(s.get("status")))
                .count();
            
            long occupiedSlots = slots.stream()
                .filter(s -> "OCCUPIED".equals(s.get("status")))
                .count();
            
            stats.put("totalBookings", bookings.size());
            stats.put("activeBookings", activeBookings);
            stats.put("totalSlots", slots.size());
            stats.put("availableSlots", availableSlots);
            stats.put("occupiedSlots", occupiedSlots);
            stats.put("revenue", bookings.size() * 100); // Simplified calculation
            
        } catch (Exception e) {
            stats.put("error", "Failed to fetch statistics: " + e.getMessage());
        }
        
        return stats;
    }
}