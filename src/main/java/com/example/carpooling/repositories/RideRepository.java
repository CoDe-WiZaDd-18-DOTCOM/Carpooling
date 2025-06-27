package com.example.carpooling.repositories;

import com.example.carpooling.entities.Ride;
import com.example.carpooling.entities.User;
import com.example.carpooling.enums.RideStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RideRepository extends MongoRepository<Ride, ObjectId> {
    List<Ride> findAllByDriver(User driver);
    List<Ride> findAllByStatus(RideStatus rideStatus);
    Optional<Ride> findById(ObjectId id);
}
