package com.banking.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PaymentHistoryDto {
    private LocalDate paymentDate;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
}
