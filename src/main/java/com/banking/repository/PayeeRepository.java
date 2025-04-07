package com.banking.repository;

import com.banking.model.Payee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayeeRepository extends JpaRepository<Payee, Long> {

    boolean existsByUser_IdAndAccountNumber(Integer userId, String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

    Optional<List<Payee>> findAllByUserId(Integer user_id);
    Optional<Payee> findByUserIdAndAccountNumber(Integer user_id, String accountNumber);

}
