package com.banking.schema;


import com.banking.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
@AllArgsConstructor
public class PaymentHistoryDto {
    private String formattedPaymentDate;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String fromAccountName;
    private String toAccountName;
    private String maskedFromAccountNumber;
    private String maskedToAccountNumber;
    private long fromAccountId;
    private long toAccountId;
    private String payeeName;
    public PaymentHistoryDto(LocalDate paymentDate, String fromAccount, String toAccount, BigDecimal amount, String fromAccountName, String toAccountName, String maskedFromAccountNumber, String maskedToAccountNumber, long fromAccountId, long toAccountId) {
        this.formattedPaymentDate = formatDate(paymentDate);
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.fromAccountName = fromAccountName;
        this.toAccountName = toAccountName;
        this.maskedFromAccountNumber = maskedFromAccountNumber;
        this.maskedToAccountNumber = maskedToAccountNumber;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;

    }
    private String formatDate(LocalDate paymentDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
        return paymentDate.format(formatter);
    }
}
