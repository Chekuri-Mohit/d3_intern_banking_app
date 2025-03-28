package com.banking.controller;

import com.banking.dto.AccountRequestDto;
import com.banking.dto.AccountResponseDto;
import com.banking.dto.AccountUpdateDto;
import com.banking.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//Account Controller
@CrossOrigin
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        AccountResponseDto accountResponseDto = accountService.createAccount(username, accountRequestDto);
        return ResponseEntity.ok(accountResponseDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<List<AccountResponseDto>>> getAllAccounts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<List<AccountResponseDto>> accounts = accountService.getAllAccounts(username);
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/update")
    public ResponseEntity<AccountResponseDto> updateAccount(@Valid @RequestBody AccountUpdateDto accountUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        AccountResponseDto updatedAccount = accountService.updateAccount(username, accountUpdateDto);
        return ResponseEntity.ok(updatedAccount);
    }

}
