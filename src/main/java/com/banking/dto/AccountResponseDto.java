package com.banking.dto;

import com.banking.enums.AccountType;
import lombok.Data;
//
import java.math.BigDecimal;
@Data
public class AccountResponseDto {
    private String accountName;
    private AccountType accountType;
    private BigDecimal balance;

}