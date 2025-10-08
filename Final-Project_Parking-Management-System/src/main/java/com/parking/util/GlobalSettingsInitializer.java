package com.parking.util;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.parking.model.GlobalSettings;
import com.parking.repository.GlobalSettingsRepository;

@Component
@Order(2) // Run after AdminUserInitializer
public class GlobalSettingsInitializer implements CommandLineRunner {

    @Autowired
    private GlobalSettingsRepository globalSettingsRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if global settings already exist
        if (globalSettingsRepository.findFirstByOrderByIdAsc() == null) {
            System.out.println("Initializing global settings...");
            
            // Create default global settings
            GlobalSettings settings = new GlobalSettings();
            settings.setDefaultPenaltyAmount(50.0);
            settings.setDefaultHourlyRate(10.0);
            settings.setUpdatedAt(LocalDateTime.now());
            
            globalSettingsRepository.save(settings);
            
            System.out.println("Global settings initialized successfully.");
        }
    }
}