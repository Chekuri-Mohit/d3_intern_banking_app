package com.banking.repository;

//
import com.banking.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByuserName(String username);

    boolean existsByUserName(@NotBlank(message = "User Name cannot be blank") String userName);
}
