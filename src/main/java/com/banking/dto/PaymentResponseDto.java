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
public class PaymentResponseDto {
    private Long id;
    private long fromAccountId;
    private long toAccountId;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private String status;
    private PayeeType payeeType;
    private String note;


    public PaymentResponseDto(Long id, BigDecimal amount, String note, PayeeType payeeType, LocalDate paymentDate, String status, Long id1, Long id2) {
    }
}
