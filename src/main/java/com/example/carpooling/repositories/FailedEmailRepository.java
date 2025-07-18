package com.example.carpooling.repositories;

import com.example.carpooling.entities.FailedEmail;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FailedEmailRepository extends MongoRepository<FailedEmail, String> {
}
