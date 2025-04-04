package com.banking.controller;

import com.banking.schema.UserProfileDto;
import com.banking.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    private final ProfileService profileService;

    @GetMapping("/show")
    public ResponseEntity<UserProfileDto> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserProfileDto userProfileDto = profileService.getProfile(username);
        return ResponseEntity.ok(userProfileDto);
    }

    @PutMapping("/update")
    public void updateProfile(@RequestBody UserProfileDto userProfileDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        profileService.updateProfile(username, userProfileDto);
    }
}
