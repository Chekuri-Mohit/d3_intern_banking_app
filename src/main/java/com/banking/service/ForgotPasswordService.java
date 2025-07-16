package com.banking.service;

import com.banking.schema.ErrorResponse;
import com.banking.schema.ForgotPasswordDto;
import com.banking.schema.SecurityQuestionDto;
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

    /**
     * Validates user credentials for password reset.
     * 
     * @param username The username to validate
     * @param email The email to validate
     * @return User object if validation succeeds
     * @throws IllegalArgumentException if username or email is null/empty
     * @throws RuntimeException if user not found or email doesn't match
     */
    private User validateUser(String username, String email) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        
        if (!userRepository.existsByUserName(username)) {
            throw new RuntimeException("Incorrect Username");
        }
        User user = userRepository.findByuserName(username).orElseThrow(()->new RuntimeException("User not found"));
        if (!user.getEmail().equals(email)){
            throw new RuntimeException("Incorrect Email");
        }
        return user;
    }

    /**
     * Resets user password after validating security question.
     * 
     * @param forgotPasswordDto The password reset request
     * @return ErrorResponse indicating success or failure
     * @throws IllegalArgumentException if input parameters are invalid
     * @throws RuntimeException if validation fails
     */
    public ErrorResponse forgotPassword(@Valid ForgotPasswordDto forgotPasswordDto) {
        if (forgotPasswordDto == null) {
            throw new IllegalArgumentException("Password reset request cannot be null");
        }
        if (forgotPasswordDto.getNewPassword() == null || forgotPasswordDto.getNewPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be null or empty");
        }
        if (forgotPasswordDto.getConfirmPassword() == null || forgotPasswordDto.getConfirmPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Confirm password cannot be null or empty");
        }
        
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
        return new ErrorResponse(true,"Successfully changed password");
    }

    public String getSecurityQuestion(@Valid SecurityQuestionDto securityQuestionDto) {
        User user = validateUser(securityQuestionDto.getUsername(), securityQuestionDto.getEmail());
        return user.getSecurityQuestion();

    }
}
