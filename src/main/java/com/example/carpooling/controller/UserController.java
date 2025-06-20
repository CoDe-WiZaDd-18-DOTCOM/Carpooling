package com.example.carpooling.controller;

import com.example.carpooling.dto.UpdateProfileRequest;
import com.example.carpooling.dto.UserProfileDto;
import com.example.carpooling.entities.User;
import com.example.carpooling.services.UserService;
import com.example.carpooling.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private AuthUtil authUtil;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getProfile() {
        String email = authUtil.getEmail();
        return new ResponseEntity<>(userService.getUserProfile(email), HttpStatus.OK);
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileDto> updateProfile(@RequestBody UserProfileDto profileDto) {
        String email = authUtil.getEmail();
        return new ResponseEntity<>(userService.updateUserProfile(email, profileDto), HttpStatus.OK);
    }
}


