package com.banking.controller;

import com.banking.dto.AccountRequestDto;
import com.banking.dto.AccountResponseDto;
import com.banking.dto.AccountUpdateDto;
import com.banking.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @PostMapping("/create")
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody AccountRequestDto accountRequestDto) {
        AccountResponseDto accountResponseDto = accountService.createAccount(accountRequestDto);
        return ResponseEntity.ok(accountResponseDto);
    }
    @GetMapping("/all")
    public ResponseEntity<List<AccountResponseDto>> getAllAccounts() {
        List<AccountResponseDto> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
    @PutMapping("/update")
    public ResponseEntity<AccountResponseDto> updateAccount(@Valid @RequestBody AccountUpdateDto accountUpdateDto) {
        AccountResponseDto updatedAccount = accountService.updateAccount(accountUpdateDto);
        return ResponseEntity.ok(updatedAccount);

    }

}
