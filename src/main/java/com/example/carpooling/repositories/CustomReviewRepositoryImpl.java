package com.example.carpooling.repositories;

import com.example.carpooling.dto.AvgRatingResult;
import com.example.carpooling.entities.User;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class CustomReviewRepositoryImpl implements CustomReviewRepository{

    @Autowired
    private MongoTemplate mongoTemplate;

    public double avgReview(String reviewee){
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("reviewee").is(reviewee)),
                Aggregation.group("reviewee").avg("rating").as("avgrating")
        );
        System.out.println(aggregation);

        AggregationResults<Document> results = mongoTemplate.aggregate(
                aggregation,
                "review",
                Document.class
        );

        Document result = results.getUniqueMappedResult();
        System.out.println(">>> Raw aggregation result: " + result);

        if (result != null && result.containsKey("avgrating")) {
            return ((Number) result.get("avgrating")).doubleValue();
        }

        return 0.0;

    }
}
