package com.banking.schema;

import com.banking.enums.AccountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

//
@Data
public class AccountRequestDto {
    @NotNull
    private AccountType accountType;
    @NotNull
    @Size(min = 3, max = 16, message = "Account name should contain at least 3 characters and at most 16 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Account name cannot contain special characters")
    private String accountName;

}
