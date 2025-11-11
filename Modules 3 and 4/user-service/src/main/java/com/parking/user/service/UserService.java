package com.parking.user.service;

import com.parking.user.config.JwtUtil;
import com.parking.user.model.User;
import com.parking.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(
            UserRepository userRepository,
            @Lazy PasswordEncoder passwordEncoder, // ✅ Lazy breaks circular dependency
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    // --- Signup ---
    public Map<String, Object> signup(String name, String email, String password, String role) {
        Map<String, Object> response = new HashMap<>();

        if (userRepository.existsByEmail(email)) {
            response.put("success", false);
            response.put("message", "Email already exists");
            return response;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role != null ? role : "USER");

        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole(), savedUser.getUserId());

        response.put("success", true);
        response.put("token", token);
        response.put("email", savedUser.getEmail());
        response.put("name", savedUser.getName());
        response.put("role", savedUser.getRole());
        response.put("userId", savedUser.getUserId());

        return response;
    }

    // --- Login ---
    public Map<String, Object> login(String email, String password) {
        Map<String, Object> response = new HashMap<>();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            response.put("success", false);
            response.put("message", "Invalid credentials");
            return response;
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getUserId());

        response.put("success", true);
        response.put("token", token);
        response.put("email", user.getEmail());
        response.put("name", user.getName());
        response.put("role", user.getRole());
        response.put("userId", user.getUserId());

        return response;
    }
}
