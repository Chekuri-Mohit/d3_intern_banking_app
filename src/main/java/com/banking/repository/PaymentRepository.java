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

    @Query("""
        SELECT new com.banking.schema.PaymentHistoryDto(
            p.paymentDate,
            fromAcc.accountNumber,
            toAcc.accountNumber,
            p.amount,
            '', '', '', '', fromAcc.id, toAcc.id
        )
        FROM Payment p
        JOIN p.fromAccount fromAcc
        JOIN p.toAccount toAcc
        JOIN fromAcc.user u
        WHERE u.id = :userId
        ORDER BY p.paymentDate DESC
    """)
    List<PaymentHistoryDto> findPaymentHistoryByUserId(@Param("userId") Long userId);
}