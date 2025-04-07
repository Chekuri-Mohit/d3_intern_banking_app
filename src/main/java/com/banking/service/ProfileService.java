package com.banking.service;

import com.banking.schema.UserProfileDto;
import com.banking.mapper.ProfileMapper;
import com.banking.model.User;
import com.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;

    @Autowired
    public ProfileService(UserRepository userRepository, ProfileMapper profileMapper) {
        this.userRepository = userRepository;
        this.profileMapper = profileMapper;
    }


    public void updateProfile(String username, UserProfileDto userProfileDto) {
        User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("Invalid username"));

        if (userProfileDto.getFirstName() != null && !userProfileDto.getFirstName().isBlank()) {
            user.setFirstName(userProfileDto.getFirstName());
        }
        if (userProfileDto.getLastName() != null && !userProfileDto.getLastName().isBlank()) {
            user.setLastName(userProfileDto.getLastName());
        }
        if (userProfileDto.getMiddleName() != null && !userProfileDto.getMiddleName().isBlank()) {
            user.setMiddleName(userProfileDto.getMiddleName());
        }
        if (userProfileDto.getAddressLine1() != null && !userProfileDto.getAddressLine1().isBlank()) {
            user.setAddressLine1(userProfileDto.getAddressLine1());
        }
        if (userProfileDto.getAddressLine2() != null && !userProfileDto.getAddressLine2().isBlank()) {
            user.setAddressLine2(userProfileDto.getAddressLine2());
        }
        if (userProfileDto.getAddressLine3() != null && !userProfileDto.getAddressLine3().isBlank()) {
            user.setAddressLine1(userProfileDto.getAddressLine1());
        }
        if (userProfileDto.getAddressLine3() != null && !userProfileDto.getAddressLine3().isBlank()) {
            user.setAddressLine1(userProfileDto.getAddressLine1());
        }
        if (userProfileDto.getCity() != null && !userProfileDto.getCity().isBlank()) {
            user.setCity(userProfileDto.getCity());
        }
        if (userProfileDto.getCountry() != null && !userProfileDto.getCountry().isBlank()) {
            user.setCountry(userProfileDto.getCountry());
        }
        if (userProfileDto.getZipCode() != null && !userProfileDto.getZipCode().isBlank()) {
            user.setZipCode(userProfileDto.getZipCode());
        }
        if (userProfileDto.getState() != null && !userProfileDto.getState().isBlank()) {
            user.setState(userProfileDto.getState());
        }

        if ((userProfileDto.getEmail() != null) && !userProfileDto.getEmail().isBlank()) {
            if (!userProfileDto.getEmail().equals(userProfileDto.getConfirmEmail())) {
                throw new RuntimeException("Email does not match");
            }
            user.setEmail(userProfileDto.getConfirmEmail());
        }
        userRepository.save(user);

    }

    public UserProfileDto getProfile(String username) {
        User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("Invalid username"));
        return profileMapper.convertUserToUserProfileDto(user);
    }
}
