package com.banking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
//
@Getter
@Setter
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String message;
    private LocalDateTime lastLogin;
    public JwtResponse(String messagen) {
        this.message = messagen;
    }
    public JwtResponse(String token, String message, LocalDateTime lastLogin) {
        this.token = token;
        this.message = message;
        this.lastLogin = lastLogin;
    }
}
