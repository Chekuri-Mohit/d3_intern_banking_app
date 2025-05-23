package com.banking.controller;

import com.banking.model.User;
import com.banking.repository.UserRepository;
import com.banking.schema.PaymentHistoryDto;
import com.banking.schema.PaymentRequestDto;
import com.banking.schema.PaymentResponseDto;
import com.banking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final UserRepository userRepository;

    @Autowired
    public PaymentController(PaymentService paymentService, UserRepository userRepository) {
        this.paymentService = paymentService;
        this.userRepository = userRepository;
    }
    @PostMapping("/make")
    public ResponseEntity<PaymentResponseDto> makePayment(@RequestBody PaymentRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        PaymentResponseDto response = paymentService.createPayment(username, request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/history")
    public ResponseEntity<Map<String, List<PaymentHistoryDto>>> getPaymentHistory(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, List<PaymentHistoryDto>> history= paymentService.getPaymentHistoryGroupedByDate(username);
        return ResponseEntity.ok(history);
    }
}
