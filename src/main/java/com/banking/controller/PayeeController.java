package com.banking.controller;

import com.banking.dto.PayeeRequestDto;
import com.banking.dto.PayeeResponseDto;
import com.banking.service.PayeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/payees")

public class PayeeController {
    private final PayeeService payeeService;

    public PayeeController(PayeeService payeeService) {
        this.payeeService = payeeService;
    }

    @PostMapping
    public ResponseEntity<PayeeResponseDto> createPayee(@RequestBody @Valid PayeeRequestDto payeeRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(payeeService.createPayee(username, payeeRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<List<PayeeResponseDto>>> getAllPayees() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(payeeService.getAllPayees(username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PayeeResponseDto> updatePayee(
        @PathVariable Long id, @RequestBody @Valid PayeeRequestDto payeeRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(payeeService.updatePayee(username,id, payeeRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePayee(@PathVariable Long id) {
        payeeService.deletePayee(id);
        return ResponseEntity.ok("Payee deleted successfully.");
    }
}
