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

    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final String SIGNUP_SUCCESS_MESSAGE = "Signup Successful";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid user credentials";
    private static final String USER_LOCKED_MESSAGE = "User is locked. Please contact customer service";
    private static final String INVALID_PASSWORD_MESSAGE = "Invalid password";
    private static final String USERNAME_EXISTS_MESSAGE = "Username Already Exists";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtils jwtUtils;
    private final AuthMapper authMapper;

    private static final ZoneId IST_ZONE = ZoneId.of("Asia/Kolkata");
    private static final DateTimeFormatter LOGIN_FMT =
        DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a z", Locale.ENGLISH);

    @Autowired
    public AuthService(UserRepository userRepository, JwtUtils jwtUtils, AuthMapper authMapper) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.authMapper = authMapper;
    }

    /**
     * Registers a new user in the system.
     * 
     * @param request The signup request containing user details
     * @return ErrorResponse indicating success or failure
     * @throws IllegalArgumentException if request is null or contains invalid data
     * @throws RuntimeException if username already exists
     */
    public ErrorResponse signup(@Valid SignupRequest request) {
        // Input validation
        if (request == null) {
            throw new IllegalArgumentException("Signup request cannot be null");
        }
        if (request.getUserName() == null || request.getUserName().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        if (userRepository.findByuserName(request.getUserName()).isPresent()) {
            throw new RuntimeException(USERNAME_EXISTS_MESSAGE);
        }
        User user = authMapper.SignupRequesttoUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return new ErrorResponse(true, SIGNUP_SUCCESS_MESSAGE);
    }

    /**
     * Authenticates a user and generates a JWT token.
     * 
     * @param request The login request containing username and password
     * @return JwtResponse containing token and user information, or error message
     * @throws RuntimeException if user credentials are invalid
     */
    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByuserName(request.getUserName())
            .orElseThrow(() -> new RuntimeException(INVALID_CREDENTIALS_MESSAGE));

        if (user.isLocked()) {
            return new JwtResponse(USER_LOCKED_MESSAGE);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            int attempts = user.getLoginAttempts() + 1;
            user.setLoginAttempts(attempts);
            if (attempts >= MAX_LOGIN_ATTEMPTS) {
                user.setLocked(true);
            }
            userRepository.save(user);
            return new JwtResponse(INVALID_PASSWORD_MESSAGE);
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

    /**
     * Unlocks a user account that was locked due to failed login attempts.
     * 
     * @param userName The username of the account to unlock
     * @return true if unlock was successful
     * @throws RuntimeException if user is not found
     */
    public boolean unlockUser(String userName) {
        User user = userRepository.findByuserName(userName)
            .orElseThrow(() -> new RuntimeException("Invalid Username"));
        user.setLocked(false);
        user.setLoginAttempts(0);
        userRepository.saveAndFlush(user);
        return true;
    }
}