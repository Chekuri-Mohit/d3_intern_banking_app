package com.banking.schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

//
@Data
public class AccountUpdateDto {
    @NotNull(message = "Account number is mandatory")
    private String accountNumber;
    @NotNull(message = "Account name is mandatory")
    @Size(min = 3, max = 16, message = "Account name should contain at least 3 characters and at most 16 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Account name cannot contain special characters")
    private String newAccountName;

}
