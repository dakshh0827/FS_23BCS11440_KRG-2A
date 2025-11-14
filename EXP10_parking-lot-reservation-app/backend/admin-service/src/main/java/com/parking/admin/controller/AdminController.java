package com.parking.admin.controller;

import com.parking.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
// @CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = adminService.getStatistics();
        return ResponseEntity.ok(stats);
    }
}