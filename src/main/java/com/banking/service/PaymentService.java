package com.banking.service;

import com.banking.mapper.PaymentMapper;
import com.banking.model.Account;
import com.banking.model.Payee;
import com.banking.model.Payment;
import com.banking.model.User;
import com.banking.repository.AccountRepo;
import com.banking.repository.PayeeRepository;
import com.banking.repository.PaymentRepository;
import com.banking.repository.UserRepository;
import com.banking.schema.PaymentHistoryDto;
import com.banking.schema.PaymentRequestDto;
import com.banking.schema.PaymentResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.security.auth.login.AccountNotFoundException;

@Service
@Transactional
public class PaymentService {

    private static final int ACCOUNT_MASK_LENGTH = 4;
    private static final String ACCOUNT_MASK_PREFIX = "****";
    private static final String DEFAULT_MASK = "****";

    private final PaymentRepository paymentRepository;
    private final AccountRepo accountRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;
    private final PayeeRepository payeeRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, AccountRepo accountRepository, UserRepository userRepository, PaymentMapper paymentMapper, PayeeRepository payeeRepository) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.paymentMapper = paymentMapper;
        this.payeeRepository = payeeRepository;
    }

    /**
     * Creates a new payment transaction between accounts.
     * 
     * @param username The username of the account holder making the payment
     * @param dto The payment request containing amount and account details
     * @return PaymentResponseDto containing the payment transaction details
     * @throws IllegalArgumentException if input parameters are invalid
     * @throws RuntimeException if accounts are not found or insufficient balance
     */
    @Transactional
    public PaymentResponseDto createPayment(String username, PaymentRequestDto dto) {
        // Input validation
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (dto == null) {
            throw new IllegalArgumentException("Payment request cannot be null");
        }
        if (dto.getFromAccountId() == null) {
            throw new IllegalArgumentException("From account ID cannot be null");
        }
        if (dto.getToPayeeId() == null) {
            throw new IllegalArgumentException("To payee ID cannot be null");
        }
        if (dto.getAmount() == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        User user = userRepository.findByuserName(username).orElseThrow(() ->  new UsernameNotFoundException("Username not found"));
        Integer UserID = user.getId();

        Account fromAccount= accountRepository.findById(dto.getFromAccountId()).orElseThrow(() ->  new RuntimeException("Account not found"));

        if(!UserID.equals(fromAccount.getUser().getId())) {
            throw new RuntimeException("Incorrect FROM account");
        }
        // Check if the sender has sufficient balance
        if (fromAccount.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Fetch Payee
        Payee payee = payeeRepository.findById(dto.getToPayeeId()).orElseThrow(() -> new RuntimeException("Payee not found"));
        String toAccountNumber=payee.getAccountNumber();

        // Update balances with BigDecimal for precision
        fromAccount.setBalance(fromAccount.getBalance().subtract(dto.getAmount()));
        accountRepository.save(fromAccount);

        Optional<Account> optionalToAccount = accountRepository.findByAccountNumber(toAccountNumber);
        Account toAccount=null;
        if (optionalToAccount.isPresent()) {
            toAccount = optionalToAccount.get();
            toAccount.setBalance(toAccount.getBalance().add(dto.getAmount()));
            accountRepository.save(toAccount);
        }
        Payment payment = paymentMapper.toEntity(dto);
        payment.setPayee(payee);
        payment.setFromAccount(fromAccount);
        paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }


    /**
     * Retrieves payment history for a user grouped by date.
     * 
     * @param username The username to get payment history for
     * @return Map of payment history grouped by formatted date
     * @throws UsernameNotFoundException if user is not found
     */
    public Map<String,List<PaymentHistoryDto>> getPaymentHistoryGroupedByDate(String username) {
        User user = userRepository.findByuserName(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        List<PaymentHistoryDto> history= paymentRepository.findPaymentHistoryByUserId(Long.valueOf(user.getId()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
        return history.stream().sorted(Comparator.comparing((PaymentHistoryDto dto)->
        LocalDate.parse(dto.getFormattedPaymentDate(), formatter)).reversed())
            .collect(Collectors.groupingBy(PaymentHistoryDto::getFormattedPaymentDate, LinkedHashMap::new, Collectors.toList()));
    }

    /**
     * Masks account number for security purposes, showing only last 4 digits.
     * 
     * @param accountNumber The account number to mask
     * @return Masked account number with format "****XXXX"
     */
    private String maskAccountNumber(String accountNumber) {
        if(accountNumber==null || accountNumber.length()<ACCOUNT_MASK_LENGTH) return DEFAULT_MASK;
        return ACCOUNT_MASK_PREFIX +  accountNumber.substring(accountNumber.length()-ACCOUNT_MASK_LENGTH);
    }

}
