package com.banking.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PaymentHistoryDto {
    private LocalDate paymentDate;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
}
