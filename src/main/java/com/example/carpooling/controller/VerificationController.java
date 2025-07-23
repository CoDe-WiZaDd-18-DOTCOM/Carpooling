package com.example.carpooling.controller;

import com.example.carpooling.services.VerificationService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/auth/verify")
public class VerificationController {
    private static final Logger log = LoggerFactory.getLogger(VerificationController.class);
    @Autowired
    private VerificationService verificationService;

    @PostMapping("/email")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String,String> body){
        try {
            verificationService.sendOtp(body.get("email"));
            return new ResponseEntity<>("sent successfully!!", HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Failed to send otp make sure you entered correct email address.",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String,String> body){
        try {
            boolean check=verificationService.validateOtp(body.get("email"),body.get("otp"));
            if(!check) throw new Exception("Failed to verify otp.Try again.");
            return new ResponseEntity<>("Verified successfully!!", HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Failed to verify otp.Try again.",HttpStatus.BAD_REQUEST);
        }
    }
}
