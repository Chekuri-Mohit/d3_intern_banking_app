package com.banking.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String securityQuestion;
    private String securityAnswer;
}
