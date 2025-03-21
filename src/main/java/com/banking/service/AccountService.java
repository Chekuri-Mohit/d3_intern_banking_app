package com.banking.service;

import com.banking.dto.AccountRequestDto;
import com.banking.dto.AccountResponseDto;
import com.banking.dto.AccountUpdateDto;
import com.banking.mapper.AccountMapper;
import com.banking.model.Account;
import com.banking.model.User;
import com.banking.repository.AccountRepo;
import com.banking.repository.UserRepository;
import com.banking.security.JwtUtils;
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
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Autowired
    public AccountService(AccountRepo accountRepo, AccountMapper accountMapper, JwtUtils jwtUtils, UserRepository userRepository) {
        this.accountRepo = accountRepo;
        this.accountMapper = accountMapper;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    public AccountResponseDto createAccount(String token,@Valid AccountRequestDto accountRequestDto) {
        String username = jwtUtils.extractUsername(token);
        User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("User not found"));
        Account account = accountMapper.toEntity(accountRequestDto);
        account.setUser(user);
        Account savedaccount = accountRepo.save(account);
        return accountMapper.toAccountResponseDto(savedaccount);


    }


    public List<List<AccountResponseDto>> getAllAccounts(String token) {
        String username = jwtUtils.extractUsername(token);
        User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("User not found"));
        Integer UserID=user.getId();

        Optional<List<Account>> accounts = accountRepo.findByUserId(UserID);
        List<List<AccountResponseDto>> collect = accounts.stream().map(accountMapper::toAccountResponseDtoList).collect(Collectors.toList());
        return collect;
    }

    public AccountResponseDto updateAccount(@Valid AccountUpdateDto accountUpdateDto) {
        Optional<Account> optionalAccount = accountRepo.findByAccountNumber(accountUpdateDto.getAccountNumber());
        if (optionalAccount.isEmpty()) {
            throw new RuntimeException("Account not found");
        }
        Account account = optionalAccount.get();
        account.setAccountName(accountUpdateDto.getNewAccountName());
        Account savedaccount = accountRepo.save(account);
        return accountMapper.toAccountResponseDto(savedaccount);
    }
}