package com.banking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payees")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 16)
    private String name;
    @Column(name = "nickname", length = 16)
    private String nickname;
    @Column(name = "address_line1", length = 120)
    private String addressLine1;
    @Column(name = "address_line2", length = 120)
    private String addressLine2;
    @Column(name = "city", length = 32)
    private String city;
    @Column(name = "state", length = 32)
    private String state;
    @Column(name = "zip", nullable = false, length = 11)
    private String zip;
    @Column(name = "phone_number", length = 10)
    private String phoneNumber;
    @Column(name = "account_number", nullable = false, length = 16)
    private String accountNumber;
    @Column(name = "status", nullable = false)
    private String status;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;
}
