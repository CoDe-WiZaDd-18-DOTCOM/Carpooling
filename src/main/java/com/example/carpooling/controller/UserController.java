package com.example.carpooling.controller;

import com.example.carpooling.dto.UpdateProfileRequest;
import com.example.carpooling.entities.User;
import com.example.carpooling.services.UserService;
import com.example.carpooling.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthUtil authUtil;

    @PutMapping("/update-profile")
    public ResponseEntity<User> updateProfile(@RequestBody UpdateProfileRequest request) {
        String email = authUtil.getEmail();
        User updatedUser = userService.updateUser(email, request);
        if (updatedUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}

