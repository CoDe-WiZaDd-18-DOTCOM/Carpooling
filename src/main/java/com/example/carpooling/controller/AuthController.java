package com.example.carpooling.controller;

import com.example.carpooling.dto.AuthResponse;
import com.example.carpooling.dto.LoginRequest;
import com.example.carpooling.dto.SignUpRequest;
import com.example.carpooling.entities.User;
import com.example.carpooling.services.UserService;
import com.example.carpooling.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignUpRequest signUpRequest){
        String email=signUpRequest.getEmail();
        if(userService.isUserExists(email)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(signUpRequest.getRole());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setPreferences(signUpRequest.getPreferences());

        userService.addUser(user);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setEmail(user.getEmail());
        authResponse.setJwtToken(jwtUtil.generateToken(user.getEmail()));
        authResponse.setRole(user.getRole().name());
        return new ResponseEntity<>(authResponse,HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        String email=loginRequest.getEmail().trim().toLowerCase();
        if(!userService.isUserExists(email)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User user = userService.getUser(email);
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        AuthResponse authResponse = new AuthResponse();
        authResponse.setEmail(user.getEmail());
        authResponse.setJwtToken(jwtUtil.generateToken(user.getEmail()));
        authResponse.setRole(user.getRole().name());
        return new ResponseEntity<>(authResponse,HttpStatus.OK);
    }
}
