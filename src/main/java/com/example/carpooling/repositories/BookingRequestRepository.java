package com.example.carpooling.repositories;

import com.example.carpooling.entities.BookingRequest;
import com.example.carpooling.entities.Ride;
import com.example.carpooling.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRequestRepository extends MongoRepository<BookingRequest, String> {
    Page<BookingRequest> findAllByRider(User rider, Pageable pageable);

    List<BookingRequest> findAllByDriver(User driver);
    List<BookingRequest> findAllByRide(Ride ride);

    boolean existsByRideAndRider(Ride ride,User rider);
    BookingRequest findByRideAndRider(Ride ride,User rider);


}
