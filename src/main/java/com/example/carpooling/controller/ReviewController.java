package com.example.carpooling.controller;

import com.example.carpooling.dto.ReviewDto;
import com.example.carpooling.dto.ReviewDto;
import com.example.carpooling.entities.Review;
import com.example.carpooling.services.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Review Management")
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Operation(summary = "Submit a new review")
    @PostMapping("/submit")
    public ResponseEntity<Review> submitReview(@RequestBody ReviewDto request) {
        Review review = reviewService.submitReview(request);
        return ResponseEntity.ok(review);
    }

//    @Operation(summary = "Get average rating for a user")
//    @PostMapping("/average-rating")
//    public ResponseEntity<Double> getAverageRating(@RequestBody Map<String, String> payload) {
//        String email = payload.get("email");
//        double avg = reviewService.getAverageRating(email);
//        return ResponseEntity.ok(avg);
//    }

    @Operation(summary = "checks weather the booking request is rated or not")
    @PostMapping("/check")
    public ResponseEntity<?> checkRated(@RequestBody String bookingId) {
        return new ResponseEntity<>(reviewService.checkRated(bookingId), HttpStatus.OK);
    }
}
