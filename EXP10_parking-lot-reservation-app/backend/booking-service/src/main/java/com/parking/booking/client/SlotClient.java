package com.parking.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "slot-service")
public interface SlotClient {
    
    @PutMapping("/api/slots/{id}/status")
    void updateSlotStatus(@PathVariable Long id, @RequestBody String status);
}