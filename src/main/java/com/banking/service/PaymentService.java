package com.banking.service;

import com.banking.dto.PaymentRequestDto;
import com.banking.dto.PaymentResponseDto;
import com.banking.mapper.PaymentMapper;
import com.banking.model.Account;
import com.banking.model.Payment;
import com.banking.repository.AccountRepo;
import com.banking.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountRepo accountRepository;

    public PaymentService(PaymentRepository paymentRepository, AccountRepo accountRepository) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
    }

    // Create a new payment
    public PaymentResponseDto createPayment(PaymentRequestDto dto) {
        Account fromAccount = accountRepository.findByAccountNumber(dto.getFromAccountNumber())
            .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account toAccount = accountRepository.findByAccountNumber(dto.getToAccountNumber())
            .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        // Check if the sender has sufficient balance
        if (fromAccount.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Update balances with BigDecimal for precision
        fromAccount.setBalance(fromAccount.getBalance().subtract(dto.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(dto.getAmount()));

        // Save accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Create and save payment
        Payment payment = PaymentMapper.toEntity(dto);
        payment.setFromAccount(fromAccount);
        payment.setToAccount(toAccount);

        paymentRepository.save(payment);

        return PaymentMapper.toDto(payment);
    }

    // Get all payments
    public List<PaymentResponseDto> getAllPayments() {
        return paymentRepository.findAll().stream()
            .map(PaymentMapper::toDto)
            .collect(Collectors.toList());
    }
}
