package com.example.carpooling.repositories;

import com.example.carpooling.entities.SosAlerts;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SosAlertsRepository extends MongoRepository<SosAlerts, ObjectId> {
}
