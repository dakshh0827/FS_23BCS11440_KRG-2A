package com.parking.qr.controller;

import com.parking.qr.service.QRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/qr")
// @CrossOrigin(origins = "http://localhost:5173")
public class QRController {
    
    @Autowired
    private QRService qrService;
    
    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateQR(@RequestBody Map<String, Object> request) {
        Long bookingId = ((Number) request.get("bookingId")).longValue();
        Map<String, Object> response = qrService.generateQRCode(bookingId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateQR(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        Map<String, Object> response = qrService.validateQRCode(code);
        return ResponseEntity.ok(response);
    }
}