package com.example.carpooling.controller;

import com.example.carpooling.dto.AuthResponse;
import com.example.carpooling.dto.LoginRequest;
import com.example.carpooling.dto.SignUpRequest;
import com.example.carpooling.entities.User;
import com.example.carpooling.services.UserService;
import com.example.carpooling.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication APIs", description = "Endpoints for user registration and login using JWT.")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user with provided details, encodes the password, and returns a JWT token on success. Returns 403 if the user already exists."
    )
    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        if (userService.isUserExists(email)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(signUpRequest.getRole());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setPreferences(signUpRequest.getPreferences());
        user.setEmergencyEmail(signUpRequest.getEmergencyEmail());
        user.setRating(0);
        user.setRating_count(0);

        userService.addUser(user);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setEmail(user.getEmail());
        authResponse.setJwtToken(jwtUtil.generateToken(user.getEmail(), user.getRole().name()));
        authResponse.setRole(user.getRole().name());

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Authenticate user and generate JWT",
            description = "Validates user credentials and returns a JWT token if authentication is successful. Returns 404 if the user does not exist or 400 for incorrect password."
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail().trim().toLowerCase();
        if (!userService.isUserExists(email)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User user = userService.getUser(email);
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        AuthResponse authResponse = new AuthResponse();
        authResponse.setEmail(user.getEmail());
        authResponse.setJwtToken(jwtUtil.generateToken(user.getEmail(),user.getRole().name()));
        authResponse.setRole(user.getRole().name());

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
