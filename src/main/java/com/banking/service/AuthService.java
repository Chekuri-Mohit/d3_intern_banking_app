package com.banking.service;

import com.banking.dto.JwtResponse;
import com.banking.dto.LoginRequest;
import com.banking.dto.SignupRequest;
import com.banking.model.User;
import com.banking.repository.UserRepository;
import com.banking.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
    private final JwtUtils jwtUtils;
    public AuthService(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }
    public String signup(SignupRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Username already exists";
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setSecurityQuestion(request.getSecurityQuestion());
        user.setSecurityAnswer(request.getSecurityAnswer());
        userRepository.save(user);
        return "signup successful";
    }
    public JwtResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        if(userOptional.isEmpty()){
            throw new RuntimeException("Invalid user credentials");
        }
        User user = userOptional.get();
        if(user.isLocked()){
            return new JwtResponse("User is locked. Please contact customer service");
        }
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            user.incrementFailedLoginAttempts();
            if (user.getFailedLoginAttempts() >= 3) {
                user.setLocked(true);
            }

            userRepository.save(user);
            return new JwtResponse("Invalid password");
        }
        if(user.isLocked()){
            return new JwtResponse("User is locked. Please contact customer service");
        }
        user.resetFailedLoginAttempts();
        LocalDateTime lastLogin = user.getLastLoginDate();
        user.setLastLoginDate(LocalDateTime.now());
        userRepository.save(user);
        String token = jwtUtils.generateToken(user.getUsername());
        return new JwtResponse(token, "Welcome" + user.getUsername(), lastLogin);
    }
}
