package com.banking.controller;

import com.banking.dto.PaymentHistoryDto;
import com.banking.dto.PaymentRequestDto;
import com.banking.dto.PaymentResponseDto;
import com.banking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/make")
    public ResponseEntity<PaymentResponseDto> makePayment(@RequestBody PaymentRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        PaymentResponseDto response = paymentService.createPayment(username, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<Map<LocalDate, List<PaymentHistoryDto>>> getPaymentHistoryByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<LocalDate, List<PaymentHistoryDto>> history = paymentService.getPaymentHistoryGroupedByDate(username);
        return ResponseEntity.ok(history);
    }
}
