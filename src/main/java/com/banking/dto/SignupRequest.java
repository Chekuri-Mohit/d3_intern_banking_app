package com.banking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
    private String securityQuestion;
    private String securityAnswer;
}
