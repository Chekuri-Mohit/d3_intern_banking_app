package com.banking.repository;

import com.banking.model.Account;
import com.banking.model.Payee;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayeeRepository extends JpaRepository<Payee,Long> {
    Optional<Payee> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
}
