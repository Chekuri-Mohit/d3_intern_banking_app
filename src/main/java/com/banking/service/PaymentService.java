package com.banking.service;

import com.banking.mapper.PaymentMapper;
import com.banking.model.Account;
import com.banking.model.Payment;
import com.banking.model.User;
import com.banking.repository.AccountRepo;
import com.banking.repository.PaymentRepository;
import com.banking.repository.UserRepository;
import com.banking.schema.PaymentHistoryDto;
import com.banking.schema.PaymentRequestDto;
import com.banking.schema.PaymentResponseDto;
import com.banking.security.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountRepo accountRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, AccountRepo accountRepository, UserRepository userRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.paymentMapper = paymentMapper;
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
        Payment payment = paymentMapper.toEntity(dto);
        payment.setFromAccount(fromAccount);
        payment.setToAccount(toAccount);

        paymentRepository.save(payment);

        return paymentMapper.toDto(payment);
    }

    public Map<String, List<PaymentHistoryDto>> getPaymentHistoryGroupedByDate(String username) {
        User user = userRepository.findByuserName(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        List<PaymentHistoryDto> history= paymentRepository.findPaymentHistoryByUserId(Long.valueOf(user.getId()));
        for (PaymentHistoryDto dto : history) {
            Account fromAccount=accountRepository.findById(dto.getFromAccountId()).orElse(null);
            Account toAccount=accountRepository.findById(dto.getToAccountId()).orElse(null);
            if(fromAccount!=null) {
                dto.setFromAccountName(fromAccount.getAccountName());
                dto.setMaskedFromAccountNumber(maskAccountNumber(fromAccount.getAccountNumber()));
            }
            if(toAccount!=null) {
                dto.setToAccountName(toAccount.getAccountName());
                dto.setMaskedToAccountNumber(maskAccountNumber(toAccount.getAccountNumber()));
            }
        }
        Map<String, List<PaymentHistoryDto>> grouped = history.stream().collect(Collectors.groupingBy(PaymentHistoryDto::getFormattedPaymentDate, LinkedHashMap::new, Collectors.toList()));
        Map<String, List<PaymentHistoryDto>> sorted= grouped.entrySet().stream()
            .sorted((e1, e2)->{
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
                LocalDate date1 = LocalDate.parse(e1.getKey(), dtf);
                LocalDate date2 = LocalDate.parse(e2.getKey(), dtf);
                return date2.compareTo(date1);
            })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (e1,e2)->e1,LinkedHashMap::new));
        for (List<PaymentHistoryDto> list : grouped.values()) {
            list.sort(Comparator.comparing(PaymentHistoryDto::getAmount).reversed());
        }
        return sorted;
    }

    private String maskAccountNumber(String accountNumber) {
        if(accountNumber==null || accountNumber.length()<4) return "****";
        return "****" +  accountNumber.substring(accountNumber.length()-4);
    }

}
