package com.banking.schema;


import com.banking.enums.AccountType;
import com.banking.model.Account;
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
    private String fromAccountName;
    private String fromAccountNumber;
    private BigDecimal amount;
    private String payeeName;
    private String toPayeeAccountNumber;
    private AccountType fromAccountType;

    public PaymentHistoryDto(LocalDate paymentDate, String fromAccountName, String fromAccountNumber, BigDecimal amount, String payeeName, String toPayeeAccountNumber, AccountType fromAccountType) {
        this.formattedPaymentDate = formatDate(paymentDate);
        this.fromAccountName = fromAccountName;
        this.fromAccountNumber =fromAccountNumber;
        this.amount = amount;
        this.payeeName = payeeName;
        this.toPayeeAccountNumber = toPayeeAccountNumber;
        this.fromAccountType = fromAccountType;
    }

    private String formatDate(LocalDate paymentDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
        return paymentDate.format(formatter);
    }
}
