package com.banking.dto;

import com.banking.enums.PayeeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetailDto {
    private long id;
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private String status;
    private LocalDate paymentDate;
    private String note;
    private PayeeType payeeType;

    public PaymentDetailDto(Long id, String accountNumber, String accountNumber1, LocalDate paymentDate, BigDecimal amount, String status, PayeeType payeeType, String note) {
    }
}
