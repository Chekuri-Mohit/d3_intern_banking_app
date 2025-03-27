package com.banking.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserProfileDto {
    private String firstName;

    private String middleName;

    private String lastName;


    @Email(message = "Enter a valid email")
    private String email;

    @Email(message = "Enter a valid confirmation email")
    private String confirmEmail;



    //optional
    private String address;
    private String city;
    private String state;


}

