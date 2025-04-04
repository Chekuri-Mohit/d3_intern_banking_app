package com.banking.schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SecurityQuestionDto{
    @NotBlank
    private String username;
    @NotBlank
    @Email
    private String email;
}
