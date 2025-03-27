package com.banking.service;

import com.banking.dto.PayeeRequestDto;
import com.banking.dto.PayeeResponseDto;
import com.banking.mapper.Payee2Mapper;
import com.banking.model.Payee;
import com.banking.model.User;
import com.banking.repository.AccountRepo;
import com.banking.repository.PayeeRepository;
import com.banking.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PayeeService {

    private final PayeeRepository payeeRepository;
    private final AccountRepo accountRepo;
    private final Payee2Mapper payeeMapper;
    private final UserRepository userRepository;

    public PayeeService(PayeeRepository payeeRepository, AccountRepo accountRepo, Payee2Mapper payee2Mapper, UserRepository userRepository) {
        this.payeeRepository = payeeRepository;
        this.accountRepo = accountRepo;
        this.payeeMapper = payee2Mapper;
        this.userRepository = userRepository;
    }

    public PayeeResponseDto createPayee(String username, PayeeRequestDto payeeRequestDto) {

        User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("User not found"));
        Integer userId = user.getId();
        if (!accountRepo.existsByAccountNumber(payeeRequestDto.getAccountNumber())) {
            throw new RuntimeException("Account number not found " + payeeRequestDto.getAccountNumber());
        }
        if (payeeRepository.existsByUser_IdAndAccountNumber(userId, payeeRequestDto.getAccountNumber())) {
            throw new RuntimeException("Account number already exists " + payeeRequestDto.getAccountNumber());
        }
        Payee payee = payeeMapper.toEntity(payeeRequestDto);
        payee.setUser(user);
        Payee savedPayee = payeeRepository.save(payee);
        return payeeMapper.toDto(savedPayee);
    }

    public List<List<PayeeResponseDto>> getAllPayees(String username) {
        User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("User not found"));
        Integer UserID = user.getId();

        Optional<List<Payee>> payees = payeeRepository.findAllByUserId(UserID);
        List<List<PayeeResponseDto>> collect = payees.stream().map(payeeMapper::toPayeeResponseDtoList).collect(Collectors.toList());
        return collect;
    }


    public PayeeResponseDto updatePayee(Long id, PayeeRequestDto payeeRequestDto) {

        Payee existingPayee = payeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Payee not found with id " + id));
        if (!accountRepo.existsByAccountNumber(payeeRequestDto.getAccountNumber())) {
            throw new RuntimeException("Account number not found " + payeeRequestDto.getAccountNumber());
        }
        if (!existingPayee.getAccountNumber().equals(payeeRequestDto.getAccountNumber()) && payeeRepository.existsByAccountNumber(payeeRequestDto.getAccountNumber())) {
            throw new RuntimeException("Payee with this Account number already exists " + payeeRequestDto.getAccountNumber());
        }
        Payee existing2Payee = payeeMapper.toEntity(payeeRequestDto);
        Payee updatedPayee = payeeRepository.save(existing2Payee);
        return payeeMapper.toDto(updatedPayee);
    }

    public void deletePayee(Long id) {
        if (!payeeRepository.existsById(id)) {
            throw new RuntimeException("Payee not found with id " + id);
        }
        payeeRepository.deleteById(id);
    }
}
