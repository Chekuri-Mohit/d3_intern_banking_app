package com.banking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name= "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String email;
    private String lastName;
    private String firstName;
    private String securityQuestion;
    private String securityAnswer;
    private int failedLoginAttempts=0;
    private LocalDateTime lastLoginDate;
    @Setter
    @Getter
    private boolean locked=false;
    public void incrementFailedLoginAttempts(){
        this.failedLoginAttempts++;
    }
    public void resetFailedLoginAttempts(){
        this.failedLoginAttempts=0;
    }

}
