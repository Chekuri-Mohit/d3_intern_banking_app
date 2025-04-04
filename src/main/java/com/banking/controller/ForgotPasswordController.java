package com.banking.controller;

import com.banking.schema.ErrorResponse;
import com.banking.schema.ForgotPasswordDto;
import com.banking.schema.SecurityQuestionDto;
import com.banking.service.ForgotPasswordService;
import jakarta.validation.Valid;
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
    @GetMapping("/get-security-question")
    public String getSecurityQuestion(@Valid @RequestBody SecurityQuestionDto securityQuestionDto) {
       return forgotPasswordService.getSecurityQuestion(securityQuestionDto);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<ErrorResponse> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
        return ResponseEntity.ok(forgotPasswordService.forgotPassword(forgotPasswordDto));

    }
}
