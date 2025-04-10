package com.banking.service;

import com.banking.mapper.AuthMapper;
import com.banking.model.User;
import com.banking.repository.UserRepository;
import com.banking.schema.ErrorResponse;
import com.banking.schema.JwtResponse;
import com.banking.schema.LoginRequest;
import com.banking.schema.SignupRequest;
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


@Service
@Validated
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtils jwtUtils;
    private final AuthMapper authMapper;

    private static final ZoneId IST_ZONE = ZoneId.of("GMT+05:30");
    private static final DateTimeFormatter LOGIN_FMT =
        DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a 'GMT+5:30'", Locale.ENGLISH);

    @Autowired
    public AuthService(UserRepository userRepository, JwtUtils jwtUtils, AuthMapper authMapper) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.authMapper = authMapper;
    }

    public ErrorResponse signup(@Valid SignupRequest request) {
        if (userRepository.findByuserName(request.getUserName()).isPresent()) {
            throw new RuntimeException("Username Already Exists");
        }
        User user = authMapper.SignupRequesttoUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return new ErrorResponse(true, "Signup Successful");
    }

    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByuserName(request.getUserName())
            .orElseThrow(() -> new RuntimeException("Invalid user credentials"));

        if (user.isLocked()) {
            return new JwtResponse("User is locked. Please contact customer service");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            int attempts = user.getLoginAttempts() + 1;
            user.setLoginAttempts(attempts);
            if (attempts >= 3) {
                user.setLocked(true);
            }
            userRepository.save(user);
            return new JwtResponse("Invalid password");
        }

        // reset on successful login
        user.setLoginAttempts(0);
        LocalDateTime lastLogin = user.getLastLoginDate();

        // save current login time in server's default zone
        LocalDateTime now = LocalDateTime.now();
        user.setLastLoginDate(now);
        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getUserName());

        // format lastLogin by converting from server zone → IST
        String formattedLastLogin = "";
        if (lastLogin != null) {
            ZonedDateTime serverZdt = lastLogin.atZone(ZoneId.systemDefault());
            ZonedDateTime istZdt = serverZdt.withZoneSameInstant(IST_ZONE);
            formattedLastLogin = istZdt.format(LOGIN_FMT);
        }

        return new JwtResponse(token, user.getUserName(), formattedLastLogin);
    }

    public boolean unlockUser(String userName) {
        User user = userRepository.findByuserName(userName)
            .orElseThrow(() -> new RuntimeException("Invalid Username"));
        user.setLocked(false);
        user.setLoginAttempts(0);
        userRepository.saveAndFlush(user);
        return true;
    }
}