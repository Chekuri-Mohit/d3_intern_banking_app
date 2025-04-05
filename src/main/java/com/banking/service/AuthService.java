package com.banking.service;

import com.banking.schema.ErrorResponse;
import com.banking.schema.JwtResponse;
import com.banking.schema.LoginRequest;
import com.banking.schema.SignupRequest;
import com.banking.mapper.AuthMapper;
import com.banking.model.User;
import com.banking.repository.UserRepository;
import com.banking.security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

@Service
@Validated
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtils jwtUtils;
    private final AuthMapper authMapper;

    @Autowired
    public AuthService(UserRepository userRepository, JwtUtils jwtUtils, AuthMapper authMapper) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.authMapper = authMapper;
    }

    public ErrorResponse signup(@Valid SignupRequest request) {
        Optional<User> users = userRepository.findByuserName(request.getUserName());
        if (users.isPresent()) {
            throw new RuntimeException("Username Already Exists");

        }
        User user = authMapper.SignupRequesttoUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return new ErrorResponse(true,"Signup Successful");
    }

    public JwtResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByuserName(request.getUserName());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid user credentials");
        }
        User user = userOptional.get();
        if (user.isLocked()) {
            return new JwtResponse("User is locked. Please contact customer service");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            int attempts = user.getLoginAttempts() + 1;
            user.setLoginAttempts(attempts);
            if (user.getLoginAttempts() >= 3) {
                user.setLocked(true);
            }
            userRepository.save(user);
            return new JwtResponse("Invalid password");
        }
        if (user.isLocked()) {
            return new JwtResponse("User is locked. Please contact customer service");
        }
        user.setLoginAttempts(0);
        LocalDateTime lastLogin = user.getLastLoginDate();
        LocalDateTime now = LocalDateTime.now();
        user.setLastLoginDate(now);
        userRepository.save(user);
        String token = jwtUtils.generateToken(user.getUserName());
        String formattedLastLogin="";
        if(lastLogin != null) {
            ZonedDateTime zonedDateTime = lastLogin.atZone(ZoneId.of("Asia/Kolkata"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a z", Locale.ENGLISH);
            formattedLastLogin = zonedDateTime.format(formatter);
        }
        return new JwtResponse(token, "Welcome " + user.getUserName(), formattedLastLogin);
    }
    public boolean unlockUser(String userName) {
        Optional<User> users = userRepository.findByuserName(userName);
        if (users.isEmpty()) {
            throw new RuntimeException("Invalid Username");
        }
        User user = users.get();
        user.setLocked(false);
        user.setLoginAttempts(0);
        userRepository.saveAndFlush(user);
        return true;
    }
}
