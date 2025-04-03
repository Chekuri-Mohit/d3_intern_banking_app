package com.banking.controller;

import com.banking.dto.ForgotPasswordDto;
import com.banking.dto.SecurityQuestionDto;
import com.banking.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/forgot")
public class ForgotPasswordController {
    private final ForgotPasswordService forgotPasswordService;
    @Autowired
    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }
    @GetMapping("/getQ")
    public String getSecurityQuestion(@RequestBody SecurityQuestionDto securityQuestionDto) {
       return forgotPasswordService.getSecurityQuestion(securityQuestionDto);
    }


    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        forgotPasswordService.forgotPassword(forgotPasswordDto);
        return ResponseEntity.ok("Forgot Password Sent");
    }
}
