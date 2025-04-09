package com.banking.service;

import com.banking.schema.ErrorResponse;
import com.banking.schema.PasswordUpdateDto;
import com.banking.schema.SecurityUpdateDto;
import com.banking.schema.UsernameUpdateDto;
import com.banking.model.User;
import com.banking.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class CredentialsService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;

    @Autowired
    public CredentialsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ErrorResponse updatePassword(String username, @Valid PasswordUpdateDto passwordUpdateDto) {
        User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(passwordUpdateDto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }
        if ((passwordUpdateDto.getNewPassword() != null) && !passwordUpdateDto.getNewPassword().isBlank()) {
            if (!passwordUpdateDto.getNewPassword().equals(passwordUpdateDto.getConfirmPassword())) {
                throw new RuntimeException("Passwords do not match");
            }
        }
        user.setPassword(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
        userRepository.save(user);
        return new ErrorResponse(true,"Updated password successfully");
    }

    public ErrorResponse updateUsername(String username, @Valid UsernameUpdateDto usernameUpdateDto) {
        User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(usernameUpdateDto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }
        if (userRepository.existsByUserName(usernameUpdateDto.getNewUsername())) {
            throw new RuntimeException("Username already exists");
        }
        user.setUserName(usernameUpdateDto.getNewUsername());
        userRepository.save(user);
        return new ErrorResponse(true,"Updated username successfully");
    }

    public ErrorResponse updateSecurityQA(String username, @Valid SecurityUpdateDto securityUpdateDto) {
        User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("User not found"));
        user.setSecurityQuestion(securityUpdateDto.getSecurityQuestion());
        user.setSecurityAnswer(securityUpdateDto.getSecurityAnswer());
        userRepository.save(user);
        return new ErrorResponse(true,"Updated Security question and answer successfully");
    }
}
