package com.example.carpooling.repositories;

import com.example.carpooling.entities.BookingRequest;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingRequestRepository extends MongoRepository<BookingRequest, ObjectId> {
}
