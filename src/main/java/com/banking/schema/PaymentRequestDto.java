package com.banking.schema;

import com.banking.enums.PayeeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
    private Long fromAccountId;
    private Long toPayeeId;
    private BigDecimal amount;
    public PayeeType payeeType;
    private LocalDate paymentDate;
    private String note;
    private String status;

}
