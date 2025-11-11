package com.parking.admin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

@FeignClient(name = "booking-service")
public interface BookingClient {
    
    @GetMapping("/api/bookings")
    List<Map<String, Object>> getAllBookings();
}