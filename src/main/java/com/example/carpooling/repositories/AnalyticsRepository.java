package com.example.carpooling.repositories;

import com.example.carpooling.entities.Analytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnalyticsRepository extends MongoRepository<Analytics, String> {
    Optional<Analytics> findById(String id);
}
