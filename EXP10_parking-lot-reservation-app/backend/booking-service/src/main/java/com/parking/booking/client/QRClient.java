package com.parking.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "qr-service")
public interface QRClient {
    
    @PostMapping("/api/qr/generate")
    Map<String, Object> generateQR(@RequestBody Map<String, Object> request);
}