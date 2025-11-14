package com.parking.notification.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {
    
    public Map<String, Object> sendNotification(String email, String message) {
        Map<String, Object> response = new HashMap<>();
        
        // Simulate sending notification (email/SMS)
        System.out.println("Sending notification to: " + email);
        System.out.println("Message: " + message);
        
        response.put("success", true);
        response.put("message", "Notification sent successfully");
        
        return response;
    }
}