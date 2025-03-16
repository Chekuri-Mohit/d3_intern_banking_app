package com.banking.controller;

import com.banking.dto.JwtResponse;
import com.banking.dto.LoginRequest;
import com.banking.dto.SignupRequest;
import com.banking.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private final AuthService authService;

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
}
