package com.banking.repository;

import com.banking.model.Payee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayeeRepository extends JpaRepository<Payee, Long> {

    boolean existsByUser_IdAndAccountNumber(Integer userId, String accountNumber);
    List<Payee> findByUser_IdAndIsDeletedFalse(Integer user_id);
    boolean existsByUser_IdAndIsDeletedFalseAndAccountNumber(Integer user_id, String accountNumber);

}
