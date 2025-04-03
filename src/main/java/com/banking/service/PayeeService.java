package com.banking.service;

import com.banking.dto.PayeeRequestDto;
import com.banking.dto.PayeeResponseDto;
import com.banking.mapper.Payee2Mapper;
import com.banking.model.Payee;
import com.banking.model.User;
import com.banking.repository.AccountRepo;
import com.banking.repository.PayeeRepository;
import com.banking.repository.UserRepository;
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


    public PayeeResponseDto updatePayee(String username, Long id, PayeeRequestDto payeeRequestDto) {
        User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("User not found"));
        Integer UserID = user.getId();
        Optional<Payee> payee = payeeRepository.findById(id);
        if (payee.isEmpty()) {
            throw new RuntimeException("Payee not found");
        }
        Payee payee1 = payee.get();

        if (!UserID.equals(payee1.getUser().getId())) {
            throw new RuntimeException("Incorrect Payee ID");
        }

        if (payeeRequestDto.getName() != null && !payeeRequestDto.getName().isBlank()) {
            payee1.setName(payeeRequestDto.getName());
        }
        if (payeeRequestDto.getNickname() != null && !payeeRequestDto.getNickname().isBlank()) {
            payee1.setNickname(payeeRequestDto.getNickname());
        }
        if (payeeRequestDto.getAddressLine1() != null && !payeeRequestDto.getAddressLine1().isBlank()) {
            payee1.setAddressLine1(payeeRequestDto.getAddressLine1());
        }
        if (payeeRequestDto.getAddressLine2() != null && !payeeRequestDto.getAddressLine2().isBlank()) {
            payee1.setAddressLine2(payeeRequestDto.getAddressLine2());
        }
        if (payeeRequestDto.getCity() != null && !payeeRequestDto.getCity().isBlank()) {
            payee1.setCity(payeeRequestDto.getCity());
        }
        if (payeeRequestDto.getState() != null && !payeeRequestDto.getState().isBlank()) {
            payee1.setState(payeeRequestDto.getState());
        }
        if (payeeRequestDto.getZip() != null && !payeeRequestDto.getZip().isBlank()) {
            payee1.setZip(payeeRequestDto.getZip());
        }
        if (payeeRequestDto.getPhoneNumber() != null && !payeeRequestDto.getPhoneNumber().isBlank()) {
            payee1.setPhoneNumber(payeeRequestDto.getPhoneNumber());
        }
        if (payeeRequestDto.getAccountNumber() != null && !payeeRequestDto.getAccountNumber().isBlank()) {
            if (accountRepo.existsByAccountNumber(payeeRequestDto.getAccountNumber())) {
                payee1.setAccountNumber(payeeRequestDto.getAccountNumber());
            }
        }
        Payee updatedPayee = payeeRepository.save(payee1);
        return payeeMapper.toDto(updatedPayee);


    }

    public void deletePayee(Long id) {
        if (!payeeRepository.existsById(id)) {
            throw new RuntimeException("Payee not found with id " + id);
        }
        payeeRepository.deleteById(id);
    }
}
