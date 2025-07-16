package com.banking.service;

import com.banking.schema.AccountRequestDto;
import com.banking.schema.AccountResponseDto;
import com.banking.schema.AccountUpdateDto;
import com.banking.mapper.AccountMapper;
import com.banking.model.Account;
import com.banking.model.User;
import com.banking.repository.AccountRepo;
import com.banking.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AccountService {
    private final AccountRepo accountRepo;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;

    @Autowired
    public AccountService(AccountRepo accountRepo, AccountMapper accountMapper,UserRepository userRepository) {
        this.accountRepo = accountRepo;
        this.accountMapper = accountMapper;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new account for a user.
     * 
     * @param username The username of the account holder
     * @param accountRequestDto The account creation request containing account details
     * @return AccountResponseDto containing the created account information
     * @throws IllegalArgumentException if input parameters are invalid
     * @throws RuntimeException if user not found or account name already exists
     */
    public AccountResponseDto createAccount(String username, @Valid AccountRequestDto accountRequestDto) {
        // Input validation
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (accountRequestDto == null) {
            throw new IllegalArgumentException("Account request cannot be null");
        }
        if (accountRequestDto.getAccountName() == null || accountRequestDto.getAccountName().trim().isEmpty()) {
            throw new IllegalArgumentException("Account name cannot be null or empty");
        }

        User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("User not found"));
        Integer userId = user.getId();
        if (accountRepo.existsByUser_IdAndAccountName(userId, accountRequestDto.getAccountName())) {
            throw new RuntimeException("Duplicate account name " + accountRequestDto.getAccountName());
        }
        Account account = accountMapper.toEntity(accountRequestDto);
        account.setUser(user);
        Account savedaccount = accountRepo.save(account);
        return accountMapper.toAccountResponseDto(savedaccount);

    }


    /**
     * Retrieves all accounts for a user.
     * 
     * @param username The username to get accounts for
     * @return List of AccountResponseDto containing user's accounts
     * @throws IllegalArgumentException if username is null or empty
     * @throws RuntimeException if user is not found
     */
    public List<List<AccountResponseDto>> getAllAccounts(String username) {
        // Input validation
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("User not found"));
        Integer UserID = user.getId();
        Optional<List<Account>> accounts = accountRepo.findByUserId(UserID);
        List<List<AccountResponseDto>> collect = accounts.stream().map(accountMapper::toAccountResponseDtoList).collect(Collectors.toList());
        return collect;
    }


    public AccountResponseDto updateAccount(String username, @Valid AccountUpdateDto accountUpdateDto) {

        User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("User not found"));
        Integer userId = user.getId();

        Optional<Account> optionalAccount = accountRepo.findByAccountNumber(accountUpdateDto.getAccountNumber());
        if (optionalAccount.isEmpty()) {
            throw new RuntimeException("Account not found");
        }
        Account account = optionalAccount.get();

        if (!userId.equals(account.getUser().getId())) {
            throw new RuntimeException("Account doesn't belong to the user");
        }
        if (accountRepo.existsByUser_IdAndAccountName(userId, accountUpdateDto.getNewAccountName())) {
            throw new RuntimeException("Duplicate account name " + accountUpdateDto.getNewAccountName());
        }
        account.setAccountName(accountUpdateDto.getNewAccountName());
        Account savedaccount = accountRepo.save(account);
        return accountMapper.toAccountResponseDto(savedaccount);
    }
}