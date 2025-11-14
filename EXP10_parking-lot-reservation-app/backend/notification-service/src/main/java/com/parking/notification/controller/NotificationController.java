package com.parking.notification.controller;

import com.parking.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
// @CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @PostMapping("/notify")
    public ResponseEntity<Map<String, Object>> sendNotification(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String message = request.get("message");
        
        Map<String, Object> response = notificationService.sendNotification(email, message);
        return ResponseEntity.ok(response);
    }
}