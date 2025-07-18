package com.example.carpooling.services;

import com.example.carpooling.dto.EmailDto;
import com.example.carpooling.entities.FailedEmail;
import com.example.carpooling.repositories.FailedEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FailedEmailService {

    @Autowired
    private FailedEmailRepository failedEmailRepository;

    public void saveFailedEmail(EmailDto dto, String reason) {
        FailedEmail failedEmail = new FailedEmail();
        failedEmail.setEmail(dto.getEmail());
        failedEmail.setSubject(dto.getSubject());
        failedEmail.setBody(dto.getBody());
        failedEmail.setReason(reason);
        failedEmail.setTimestamp(LocalDateTime.now());

        failedEmailRepository.save(failedEmail);
    }

    public List<FailedEmail> getAllFailedEmails() {
        return failedEmailRepository.findAll();
    }

    public boolean deleteById(String id) {
        Optional<FailedEmail> failedEmail = failedEmailRepository.findById(id);
        if (failedEmail.isPresent()) {
            failedEmailRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
