package com.banking.schema;

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
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    private String city;
    private String state;
    private String zipCode;
    private String country;


}

