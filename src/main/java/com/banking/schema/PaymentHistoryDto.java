package com.banking.schema;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PaymentHistoryDto {
    private LocalDate paymentDate;
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
}
