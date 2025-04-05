package com.banking.schema;

import lombok.Data;

@Data
public class SecurityUpdateDto {
    private String securityQuestion;
    private String securityAnswer;
}
