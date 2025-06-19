package com.example.carpooling.repositories;

import com.example.carpooling.entities.Ride;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RideRepository extends MongoRepository<Ride, ObjectId> {
}
