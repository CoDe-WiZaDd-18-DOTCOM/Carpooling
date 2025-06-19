package com.example.carpooling.repositories;

import com.example.carpooling.entities.Ride;
import com.example.carpooling.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RideRepository extends MongoRepository<Ride, ObjectId> {
    List<Ride> findAllByDriver(User driver);
}
