package com.banking.dto;

import com.banking.enums.PayeeType;
import com.banking.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    public PayeeType payeeType;
    private LocalDate paymentDate;
    private String note;
    private String status;
}
