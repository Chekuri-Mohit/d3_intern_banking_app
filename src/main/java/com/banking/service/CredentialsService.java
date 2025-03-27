package com.banking.service;

import com.banking.dto.PasswordUpdateDto;
import com.banking.dto.SecurityUpdateDto;
import com.banking.dto.UsernameUpdateDto;
import com.banking.model.User;
import com.banking.repository.UserRepository;
import com.banking.security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class CredentialsService {

    private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Autowired
    public CredentialsService(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    public void updatePassword(String username, @Valid PasswordUpdateDto passwordUpdateDto) {
        User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("User not found"));
        if(!passwordEncoder.matches(passwordUpdateDto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }
        user.setPassword(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
        userRepository.save(user);
    }
    public void updateUsername(String username, @Valid UsernameUpdateDto usernameUpdateDto) {
        User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("User not found"));
        if(!passwordEncoder.matches(usernameUpdateDto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }
        if(userRepository.existsByUserName(usernameUpdateDto.getNewUsername())) {
            throw new RuntimeException("Username already exists");
        }
        user.setUserName(usernameUpdateDto.getNewUsername());
        userRepository.save(user);

    }

    public void updateSecurityQA(String username, @Valid SecurityUpdateDto securityUpdateDto) {
        User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("User not found"));
        user.setSecurityQuestion(securityUpdateDto.getSecurityQuestion());
        user.setSecurityAnswer(securityUpdateDto.getSecurityAnswer());
        userRepository.save(user);
    }
}
