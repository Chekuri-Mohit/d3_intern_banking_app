package com.banking.schema;

import com.banking.enums.PayeeType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@NoArgsConstructor
public class PaymentResponseDto {
    private long id;
    private long fromAccountId;
    private long toAccountId;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private String status;
    private PayeeType payeeType;
    private String note;

    public PaymentResponseDto(long id, long fromAccountId, long toAccountId, LocalDate paymentDate, BigDecimal amount, String status, PayeeType payeeType, String note) {
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.status = status;
        this.payeeType = payeeType;
        this.note = note;
    }


}
