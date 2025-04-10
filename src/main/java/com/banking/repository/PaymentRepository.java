package com.banking.repository;


import com.banking.schema.PaymentHistoryDto;
import com.banking.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT new com.banking.schema.PaymentHistoryDto(" +
        "p.paymentDate, " +
        "fa.accountName, " +
        "fa.accountNumber, " +
        "p.amount, " +
        "pa.name, " +
        "pa.accountNumber, " +
        "fa.accountType) " +
        "FROM Payment p " +
        "JOIN p.fromAccount fa " +
        "JOIN p.payee pa " +
        "WHERE fa.user.id = :userId")
    List<PaymentHistoryDto> findPaymentHistoryByUserId(Long userId);

}