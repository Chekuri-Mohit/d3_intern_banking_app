package com.banking.controller;

import com.banking.dto.PaymentHistoryDto;
import com.banking.dto.PaymentRequestDto;
import com.banking.dto.PaymentResponseDto;
import com.banking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping("/history/{userId}")
    public ResponseEntity<Map<LocalDate, List<PaymentHistoryDto>>> getPaymentHistoryByUserId(@PathVariable long userId) {
        Map<LocalDate, List<PaymentHistoryDto>> history= paymentService.getPaymentHistoryGroupedByDate(userId);
        return ResponseEntity.ok(history);
    }
}
