package com.banking.model;

import com.banking.enums.PayeeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="from_account_id", nullable=false)
    private Account fromAccount;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="to_account_id", nullable=false)
    private Account toAccount;

    @Enumerated(EnumType.STRING)
    @Column(name="payee_type", nullable=false)
    private PayeeType payeeType;

    @Column(name="amount", nullable=false)
    private BigDecimal amount;

    @Column(name="payment_date", nullable=false)
    private LocalDate paymentDate;

    @Column(name="note")
    private String note;

    @Column(name="status", nullable=false)
    private String status="PENDING";



    }
