package com.example.carpooling.repositories;

import com.example.carpooling.entities.SosAuthorities;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SosAuthoritesRepository extends MongoRepository<SosAuthorities, String> {
    SosAuthorities findByLabel(String label);
}
