package com.banking.service;

import com.banking.dto.ForgotPasswordDto;
import com.banking.dto.SecurityQuestionDto;
import com.banking.model.User;
import com.banking.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ForgotPasswordService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public ForgotPasswordService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private User validateUser(String username, String email) {
        if (!userRepository.existsByUserName(username)) {
            throw new RuntimeException("Incorrect Username");
        }
        User user = userRepository.findByuserName(username).orElseThrow(()->new RuntimeException("User not found"));
        if (!user.getEmail().equals(email)){
            throw new RuntimeException("Incorrect Email");
        }
        return user;
    }

    public void forgotPassword(@Valid ForgotPasswordDto forgotPasswordDto) {
       User user = validateUser(forgotPasswordDto.getUsername(), forgotPasswordDto.getEmail());
        if(!user.getSecurityAnswer().equals(forgotPasswordDto.getSecurityAnswer())){
            throw new RuntimeException("Incorrect Security Answer");
        }
        if(forgotPasswordDto.getNewPassword().equals(forgotPasswordDto.getConfirmPassword())){
            user.setPassword(passwordEncoder.encode(forgotPasswordDto.getNewPassword()));
        }else{
            throw new RuntimeException("Passwords do not match");
        }
        userRepository.save(user);

    }

    public String getSecurityQuestion(@Valid SecurityQuestionDto securityQuestionDto) {
        User user = validateUser(securityQuestionDto.getUsername(), securityQuestionDto.getEmail());
        return user.getSecurityQuestion();

    }
}
