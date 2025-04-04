package com.banking.controller;

import com.banking.schema.ErrorResponse;
import com.banking.schema.PasswordUpdateDto;
import com.banking.schema.SecurityUpdateDto;
import com.banking.schema.UsernameUpdateDto;
import com.banking.service.CredentialsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/credentials")
public class CredentialsController {
    private final CredentialsService credentialsService;

    @Autowired
    public CredentialsController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<ErrorResponse> updatePassword(@Valid @RequestBody PasswordUpdateDto passwordUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(credentialsService.updatePassword(username, passwordUpdateDto));

    }

    @PutMapping("/updateUsername")
    public ResponseEntity<ErrorResponse> updateUsername(@Valid @RequestBody UsernameUpdateDto usernameUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ResponseEntity.ok(credentialsService.updateUsername(username, usernameUpdateDto));

    }

    @PutMapping("/updateSecurityQA")
    public ResponseEntity<ErrorResponse> updateSecurityQA(@Valid @RequestBody SecurityUpdateDto securityUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(credentialsService.updateSecurityQA(username, securityUpdateDto));

    }
}
