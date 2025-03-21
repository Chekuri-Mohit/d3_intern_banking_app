package com.banking.controller;

import com.banking.dto.JwtResponse;
import com.banking.dto.LoginRequest;
import com.banking.dto.SignupRequest;
import com.banking.dto.UnlockDto;
import com.banking.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//
@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request){
        System.out.println("sign up success");
        return authService.signup(request);
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request){
        System.out.println("login success");
        return authService.login(request);
    }

    @PostMapping("/unlock")
    public ResponseEntity<String> unlockUser(@RequestBody @Valid UnlockDto unlockDto) {
        boolean isUnlocked = authService.unlockUser(unlockDto.getUserName());
        if (isUnlocked) {
            return ResponseEntity.ok("Successfully Unlocked");
        }else {
            return ResponseEntity.badRequest().body("Unlock Failed");
        }
    }

}
