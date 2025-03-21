package com.banking.controller;

import com.banking.dto.PasswordUpdateDto;
import com.banking.dto.SecurityUpdateDto;
import com.banking.dto.UsernameUpdateDto;
import com.banking.service.CredentialsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> updatePassword(@RequestHeader("Authorization") String authHeader,@Valid @RequestBody PasswordUpdateDto passwordUpdateDto) {
        String token = authHeader.replace("Bearer ","");
        credentialsService.updatePassword(token,passwordUpdateDto);
        return ResponseEntity.ok("Successfully updated password");

    }
    @PutMapping("/updateUsername")
    public ResponseEntity<String> updateUsername(@RequestHeader("Authorization") String authHeader,@Valid @RequestBody UsernameUpdateDto usernameUpdateDto) {
        String token = authHeader.replace("Bearer ","");
        credentialsService.updateUsername(token,usernameUpdateDto);
        return ResponseEntity.ok("Successfully updated username");
    }
    @PutMapping("/updateSecurityQA")
    public ResponseEntity<String> updateSecurityQA(@RequestHeader("Authorization") String authHeader,@Valid @RequestBody SecurityUpdateDto securityUpdateDto) {
        String token = authHeader.replace("Bearer ","");
        credentialsService.updateSecurityQA(token,securityUpdateDto);
        return ResponseEntity.ok("Successfully updated security QA");
    }
}
