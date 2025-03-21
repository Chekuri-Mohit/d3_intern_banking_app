package com.banking.controller;

import com.banking.dto.PaymentRequestDto;
import com.banking.dto.PaymentResponseDto;
import com.banking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @PostMapping("/make")
    public ResponseEntity<PaymentResponseDto> makePayment(@RequestBody PaymentRequestDto request) {
        PaymentResponseDto response = paymentService.createPayment(request);
        return ResponseEntity.ok(response);
    }
}
