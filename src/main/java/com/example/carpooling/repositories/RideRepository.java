package com.example.carpooling.repositories;

import com.example.carpooling.entities.Ride;
import com.example.carpooling.entities.User;
import com.example.carpooling.enums.RideStatus;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRepository extends MongoRepository<Ride, ObjectId> {
    Page<Ride> findAllByDriver(User driver, Pageable pageable);
    List<Ride> findAllByStatus(RideStatus rideStatus);
    Optional<Ride> findById(ObjectId id);
}
