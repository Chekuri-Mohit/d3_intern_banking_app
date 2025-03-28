package com.banking.repository;
//

import com.banking.model.Account;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    Optional<List<Account>> findByUserId(Integer userid);

    Optional<Account> findByAccountNumber(@NotNull(message = "Account number is mandatory") String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

}
