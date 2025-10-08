package com.parking.service;

import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parking.model.GlobalSettings;
import com.parking.repository.GlobalSettingsRepository;

@Service
public class GlobalSettingsService {

    @Autowired
    private GlobalSettingsRepository globalSettingsRepository;
    
    // private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public GlobalSettings getGlobalSettings() {
        GlobalSettings settings = globalSettingsRepository.findFirstByOrderByIdAsc();
        if (settings == null) {
            settings = new GlobalSettings();
            settings.setUpdatedAt(LocalDateTime.now());
            settings = globalSettingsRepository.save(settings);
        }
        return settings;
    }
    
    public GlobalSettings updateGlobalSettings(GlobalSettings settingsDetails) {
        GlobalSettings settings = getGlobalSettings();
        settings.setDefaultPenaltyAmount(settingsDetails.getDefaultPenaltyAmount());
        settings.setDefaultHourlyRate(settingsDetails.getDefaultHourlyRate());
        settings.setUpdatedAt(LocalDateTime.now());
        return globalSettingsRepository.save(settings);
    }
}
