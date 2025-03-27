package com.banking.controller;

import com.banking.dto.UserProfileDto;
import com.banking.repository.UserRepository;
import com.banking.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<UserProfileDto> getProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
         UserProfileDto userProfileDto = profileService.getProfile(token);
         return ResponseEntity.ok(userProfileDto);
    }
    @PutMapping("/update")
    public void updateProfile(@RequestHeader("Authorization") String authHeader, @RequestBody UserProfileDto userProfileDto) {
        String token = authHeader.replace("Bearer ", "");
        profileService.updateProfile(token,userProfileDto);
    }
}
