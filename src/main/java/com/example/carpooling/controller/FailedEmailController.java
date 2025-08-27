package com.example.carpooling.controller;

import com.example.carpooling.entities.FailedEmail;
import com.example.carpooling.services.FailedEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/failed-emails")
public class FailedEmailController {

    private static final Logger log = LoggerFactory.getLogger(FailedEmailController.class);
    @Autowired
    private FailedEmailService failedEmailService;

    @GetMapping
    public ResponseEntity<List<FailedEmail>> getAllFailedEmails() {
        try {
            return ResponseEntity.ok(failedEmailService.getAllFailedEmails());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFailedEmail(@PathVariable String id) {
        try {
            boolean deleted = failedEmailService.deleteById(id);
            if (deleted) {
                return ResponseEntity.ok("Deleted successfully");
            } else {
                return ResponseEntity.status(404).body("Failed email not found");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
