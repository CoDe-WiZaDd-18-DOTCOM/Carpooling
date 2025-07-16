package com.example.carpooling.repositories;

import com.example.carpooling.entities.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review,String>, CustomReviewRepository {
    List<Review> findAllByReviewee(String reviewee);
}
