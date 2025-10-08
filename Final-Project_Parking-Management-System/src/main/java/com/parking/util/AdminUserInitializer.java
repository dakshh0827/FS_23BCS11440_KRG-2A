package com.parking.util;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.parking.model.User;
import com.parking.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if admin user exists
        Optional<User> adminUser = userRepository.findByUsername("admin");
        
        if (!adminUser.isPresent()) {
            log.info("No admin user found. Creating default admin user...");
            createAdminUser();
        } else {
            // Ensure the existing admin user has the ROLE_ADMIN role
            User existingAdmin = adminUser.get();
            Set<String> roles = existingAdmin.getRoles();
            
            if (!roles.contains("ROLE_ADMIN")) {
                log.info("Updating admin user with ROLE_ADMIN role...");
                roles.add("ROLE_ADMIN");
                existingAdmin.setRoles(roles);
                userRepository.save(existingAdmin);
            }
        }
    }
    
    private void createAdminUser() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@parking.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_ADMIN");
        roles.add("ROLE_USER"); // Admin also has user privileges
        admin.setRoles(roles);
        
        // Set timestamps using LocalDateTime directly
        LocalDateTime now = LocalDateTime.now();
        admin.setCreatedAt(now);
        admin.setUpdatedAt(now);
        
        userRepository.save(admin);
        log.info("Default admin user created successfully.");
    }
}
