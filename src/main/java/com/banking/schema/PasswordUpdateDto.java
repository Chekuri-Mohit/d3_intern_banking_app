package com.banking.schema;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PasswordUpdateDto {
    private String currentPassword;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,25}$",message = "Password must be 8-25 characters long, include uppercase, lowercase, digit and special character")
    private String newPassword;
    private String confirmPassword;

    @AssertTrue(message = "Both Passwords must match")
    public boolean isPasswordMatch() {
        return  confirmPassword.equals(newPassword);
    }
}
