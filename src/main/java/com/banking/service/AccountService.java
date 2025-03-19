package com.banking.service;

import com.banking.dto.AccountRequestDto;
import com.banking.dto.AccountResponseDto;
import com.banking.dto.AccountUpdateDto;
import com.banking.mapper.AccountMapper;
import com.banking.model.Account;
import com.banking.repository.AccountRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;




@Service
public class AccountService {
    private final AccountRepo accountRepo;
    private final AccountMapper accountMapper;

//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    if(authentication!=null && authentication.getPrincipal() instanceof OAuth2ResourceServerProperties.Jwt)
    @Autowired
    public AccountService(AccountRepo accountRepo, AccountMapper accountMapper) {
        this.accountRepo = accountRepo;
        this.accountMapper = accountMapper;
    }

    public AccountResponseDto createAccount(@Valid AccountRequestDto accountRequestDto) {
        Account account = accountMapper.toEntity(accountRequestDto);
        Account savedaccount = accountRepo.save(account);
        return accountMapper.toAccountResponseDto(savedaccount);


    }

    public List<AccountResponseDto> getAllAccounts() {
        List<Account> accounts = accountRepo.findAll();
        return accounts.stream().map(accountMapper::toAccountResponseDto).collect(Collectors.toList());
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