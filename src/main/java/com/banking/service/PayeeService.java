package com.banking.service;

import com.banking.dto.PayeeRequestDto;
import com.banking.dto.PayeeResponseDto;
import com.banking.mapper.PayeeMapper;
import com.banking.model.Payee;
import com.banking.repository.AccountRepo;
import com.banking.repository.PayeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayeeService {

    private final PayeeRepository payeeRepository;
    private final AccountRepo accountRepo;
    private final PayeeMapper payeeMapper;

    public PayeeService(PayeeRepository payeeRepository, AccountRepo accountRepo, PayeeMapper payeeMapper) {
        this.payeeRepository = payeeRepository;
        this.accountRepo = accountRepo;
        this.payeeMapper = payeeMapper;
    }
    public PayeeResponseDto createPayee(PayeeRequestDto payeeRequestDto) {
        if (!accountRepo.existsByAccountNumber(payeeRequestDto.getAccountNumber())) {
            throw new RuntimeException("Account number not found " + payeeRequestDto.getAccountNumber());
        }
        if (payeeRepository.existsByAccountNumber(payeeRequestDto.getAccountNumber())) {
            throw new RuntimeException("Account number already exists " + payeeRequestDto.getAccountNumber());
        }

        Payee payee = payeeMapper.toEntity(payeeRequestDto);
        Payee savedPayee = payeeRepository.save(payee);
        return payeeMapper.toDto(savedPayee);
    }
    public List<PayeeResponseDto> getAllPayees() {
        return payeeRepository.findAll().stream().map(payeeMapper::toDto).collect(Collectors.toList());
    }
    public PayeeResponseDto updatePayee(Long id, PayeeRequestDto payeeRequestDto) {
        Payee existingPayee = payeeRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Payee not found with id " + id));
        if (!accountRepo.existsByAccountNumber(payeeRequestDto.getAccountNumber())) {
            throw new RuntimeException("Account number not found " + payeeRequestDto.getAccountNumber());
        }
        if(!existingPayee.getAccountNumber().equals(payeeRequestDto.getAccountNumber()) && payeeRepository.existsByAccountNumber(payeeRequestDto.getAccountNumber())) {
            throw new RuntimeException("Payee with this Account number already exists " + payeeRequestDto.getAccountNumber());
        }
        existingPayee.setAccountNumber(payeeRequestDto.getAccountNumber());
        existingPayee.setName(payeeRequestDto.getName());
        existingPayee.setNickname(payeeRequestDto.getNickname());
        existingPayee.setAddressLine1(payeeRequestDto.getAddressLine1());
        existingPayee.setAddressLine2(payeeRequestDto.getAddressLine2());
        existingPayee.setCity(payeeRequestDto.getCity());
        existingPayee.setState(payeeRequestDto.getState());
        existingPayee.setPhoneNumber(existingPayee.getPhoneNumber());
        existingPayee.setZip(payeeRequestDto.getZip());
        existingPayee.setStatus(payeeRequestDto.getStatus());
        Payee updatedPayee = payeeRepository.save(existingPayee);
        return payeeMapper.toDto(updatedPayee);
    }
    public void deletePayee(Long id) {
        if (!payeeRepository.existsById(id)) {
            throw new RuntimeException("Payee not found with id " + id);
        }
        payeeRepository.deleteById(id);
    }
}
