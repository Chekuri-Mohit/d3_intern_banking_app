package com.banking.service;

import com.banking.dto.PaymentHistoryDto;
import com.banking.dto.PaymentRequestDto;
import com.banking.dto.PaymentResponseDto;
import com.banking.mapper.PaymentMapper;
import com.banking.model.Account;
import com.banking.model.Payment;
import com.banking.model.User;
import com.banking.repository.AccountRepo;
import com.banking.repository.PaymentRepository;
import com.banking.repository.UserRepository;
import com.banking.security.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountRepo accountRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, AccountRepo accountRepository, JwtUtils jwtUtils, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    // Create a new payment
    @Transactional
    public PaymentResponseDto createPayment(String username, PaymentRequestDto dto) {

        User user = userRepository.findByuserName(username).orElseThrow(() ->  new UsernameNotFoundException("Username not found"));
        Integer UserID = user.getId();

        Account fromAccount = accountRepository.findByAccountNumber(dto.getFromAccountNumber())
            .orElseThrow(() -> new RuntimeException("Sender account not found"));

        if(!UserID.equals(fromAccount.getUser().getId())) {
            throw new RuntimeException("Incorrect FROM account");
        }
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
    public List<PaymentResponseDto> getAllPayments(String username) {

        return paymentRepository.findAll().stream()
            .map(PaymentMapper::toDto)
            .collect(Collectors.toList());
    }
    public Map<LocalDate, List<PaymentHistoryDto>> getPaymentHistoryGroupedByDate(long userId) {
        List<PaymentHistoryDto> history= paymentRepository.findPaymentHistoryByUserId(userId);
        return history.stream().collect(Collectors.groupingBy(PaymentHistoryDto::getPaymentDate));
    }
/*
    public List<PaymentHistoryDto> getPaymentHistory(long id) {
        List<Payment> payments = paymentRepository.findByFromAccountUserId(id);

        payments.forEach(payment -> {
            payment.getFromAccount().getAccountNumber();
            payment.getToAccount().getAccountNumber();
        });
        Map<java.time.LocalDate, List<Payment>> groupedPayments = payments.stream().collect(Collectors.groupingBy(Payment::getPaymentDate));
        return groupedPayments.entrySet().stream()
            .map(entry-> new PaymentHistoryDto(
                entry.getKey(),
                entry.getValue().stream()
                    .map(PaymentMapper::toDetailDto)
                    .collect(Collectors.toList())
            ))
            .collect(Collectors.toList());
    }

*/
}
