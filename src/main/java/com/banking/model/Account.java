package com.banking.model;
import com.banking.enums.AccountType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name="accounts")
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number",nullable = false, unique = true,length = 16)
    private String accountNumber;

    @Column(name = "account_name",nullable = false,length = 16)
    private String accountName;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type",nullable = false)
    private AccountType accountType;

    @Column(name = "balance",nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name="userID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "fromAccount", cascade= CascadeType.ALL, orphanRemoval = true)
    private List<Payment> outgoingPayments;

    @OneToMany(mappedBy = "toAccount", cascade= CascadeType.ALL, orphanRemoval = true)
    private List<Payment> incomingPayments;
}
