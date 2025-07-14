package com.example.carpooling.controller;

import com.example.carpooling.dto.UpdateProfileRequest;
import com.example.carpooling.dto.UserProfileDto;
import com.example.carpooling.entities.User;
import com.example.carpooling.enums.Role;
import com.example.carpooling.services.UserService;
import com.example.carpooling.utils.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "APIs related to user profile details, updates, profile picture upload, and admin operations.")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private AuthUtil authUtil;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Operation(
            summary = "Get current user profile",
            description = "Returns the profile information of the currently authenticated user."
    )
    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getProfile() {
        String email = authUtil.getEmail();
        return new ResponseEntity<>(userService.getUserProfile(email), HttpStatus.OK);
    }

    @Operation(
            summary = "Update current user profile",
            description = "Allows the authenticated user to update their profile information such as name, phone, preferences, etc."
    )
    @PutMapping("/me")
    public ResponseEntity<UserProfileDto> updateProfile(@RequestBody UserProfileDto profileDto) {
        String email = authUtil.getEmail();
        return new ResponseEntity<>(userService.updateUserProfile(email, profileDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Upload profile picture",
            description = "Uploads and stores the profile picture of the current user as a Base64-encoded image."
    )
    @PostMapping("/upload-picture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        try {
            User user = userService.getUser(authUtil.getEmail());

            byte[] bytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(bytes);

            user.setProfileImageBase64(base64Image);
            userService.addUser(user);

            return ResponseEntity.ok("Profile picture uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Get all users list",
            description = "Returns all the users in db. Require admin role to access it."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user-list")
    public ResponseEntity<?> getUsersList() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "promote user", description = "promote user from user to admin")
    @PostMapping("/user/{email}")
    public ResponseEntity<?> promoteUser(@PathVariable String email){
        try {
            User user = userService.getUser(email);
            user.setRole(Role.ADMIN);
            userService.addUser(user);
            logger.info("new user promted to admin: "+email);
            return ResponseEntity.ok("promoted");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
