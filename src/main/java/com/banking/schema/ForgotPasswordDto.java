package com.banking.schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ForgotPasswordDto extends SecurityQuestionDto {
    @NotBlank
    private String securityAnswer;
    @NotBlank
    private String newPassword;
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,25}$", message = "Password must be 8-25 characters long, include uppercase, lowercase, digit and special character")
    private String confirmPassword;


}
