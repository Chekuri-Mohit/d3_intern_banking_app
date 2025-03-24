package com.banking.controller;

import com.banking.dto.AccountRequestDto;
import com.banking.dto.AccountResponseDto;
import com.banking.dto.AccountUpdateDto;
import com.banking.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<AccountResponseDto> createAccount(@RequestHeader("Authorization") String authHeader,@Valid @RequestBody AccountRequestDto accountRequestDto) {
        String token = authHeader.replace("Bearer ","");
        AccountResponseDto accountResponseDto = accountService.createAccount(token,accountRequestDto);
        return ResponseEntity.ok(accountResponseDto);
    }
    @GetMapping("/all")
    public ResponseEntity<List<List<AccountResponseDto>>> getAllAccounts(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ","");
        List<List<AccountResponseDto>> accounts = accountService.getAllAccounts(token);
        return ResponseEntity.ok(accounts);
    }
    @PutMapping("/update")
    public ResponseEntity<AccountResponseDto> updateAccount(@RequestHeader("Authorization") String authHeader,@Valid @RequestBody AccountUpdateDto accountUpdateDto) {
        String token = authHeader.replace("Bearer ","");
        AccountResponseDto updatedAccount = accountService.updateAccount(token,accountUpdateDto);
        return ResponseEntity.ok(updatedAccount);

    }

}
