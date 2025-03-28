package com.banking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank(message = "First Name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    private String lastName;

    @NotBlank(message = "User Name cannot be blank")
    private String userName;

    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,25}$", message = "Password must be 8-25 characters long, include uppercase, lowercase, digit and special character")
    private String password;

    @NotBlank(message = "Security Question cannot be blank")
    private String securityQuestion;

    @NotBlank(message = "Security Answer cannot be blank")
    private String securityAnswer;
}
