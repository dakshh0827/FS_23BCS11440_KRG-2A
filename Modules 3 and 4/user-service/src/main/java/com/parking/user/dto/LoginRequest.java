package com.parking.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Login Request
@Data
@NoArgsConstructor
@AllArgsConstructor
class LoginRequest {
    private String email;
    private String password;
}