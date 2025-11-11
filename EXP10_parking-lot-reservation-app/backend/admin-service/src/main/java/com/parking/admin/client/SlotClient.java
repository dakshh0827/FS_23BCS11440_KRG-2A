package com.parking.admin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

@FeignClient(name = "slot-service")
public interface SlotClient {
    
    @GetMapping("/api/slots")
    List<Map<String, Object>> getAllSlots();
}