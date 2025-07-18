package com.example.carpooling.controller;

import com.example.carpooling.entities.FailedEmail;
import com.example.carpooling.services.FailedEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/failed-emails")
public class FailedEmailController {

    @Autowired
    private FailedEmailService failedEmailService;

    @GetMapping
    public ResponseEntity<List<FailedEmail>> getAllFailedEmails() {
        return ResponseEntity.ok(failedEmailService.getAllFailedEmails());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFailedEmail(@PathVariable String id) {
        boolean deleted = failedEmailService.deleteById(id);
        if (deleted) {
            return ResponseEntity.ok("Deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Failed email not found");
        }
    }
}
