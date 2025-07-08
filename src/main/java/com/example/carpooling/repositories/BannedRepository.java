package com.example.carpooling.repositories;

import com.example.carpooling.entities.Banned;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BannedRepository extends MongoRepository<Banned, ObjectId> {
    boolean existsByEmail(String email);
    Banned findByEmail(String email);

    void deleteByEmail(String email);
}
