package com.banking.controller;

import com.banking.dto.PasswordUpdateDto;
import com.banking.dto.SecurityUpdateDto;
import com.banking.dto.UsernameUpdateDto;
import com.banking.service.CredentialsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<String> updatePassword(@Valid @RequestBody PasswordUpdateDto passwordUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        credentialsService.updatePassword(username,passwordUpdateDto);
        return ResponseEntity.ok("Successfully updated password");

    }
    @PutMapping("/updateUsername")
    public ResponseEntity<String> updateUsername(@Valid @RequestBody UsernameUpdateDto usernameUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        credentialsService.updateUsername(username,usernameUpdateDto);
        return ResponseEntity.ok("Successfully updated username");
    }
    @PutMapping("/updateSecurityQA")
    public ResponseEntity<String> updateSecurityQA(@Valid @RequestBody SecurityUpdateDto securityUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        credentialsService.updateSecurityQA(username,securityUpdateDto);
        return ResponseEntity.ok("Successfully updated security QA");
    }
}
