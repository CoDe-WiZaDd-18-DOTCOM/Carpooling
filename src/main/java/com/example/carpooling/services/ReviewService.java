package com.example.carpooling.services;

import com.example.carpooling.dto.ReviewDto;
import com.example.carpooling.entities.BookingRequest;
import com.example.carpooling.entities.Review;
import com.example.carpooling.entities.User;
import com.example.carpooling.repositories.BookingRequestRepository;
import com.example.carpooling.repositories.ReviewRepository;
import com.example.carpooling.repositories.UserRepository;
import com.example.carpooling.utils.AuthUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRequestRepository bookingRequestRepository;

    @Autowired
    private AuthUtil authUtil;

    public Review submitReview(ReviewDto request) {
//        User reviewer = userRepository.findByEmail(authUtil.getEmail());
//        User reviewee = userRepository.findByEmail(request.getRevieweeEmail());
        String bookingId=request.getBookingId();

        Review review = new Review();
        review.setReviewer(authUtil.getEmail());
        review.setReviewee(request.getRevieweeEmail());
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);

        if(bookingId!=null && !bookingId.isEmpty()){
            BookingRequest bookingRequest = bookingRequestRepository.findById(new ObjectId(bookingId)).orElse(null);
            if(bookingRequest!=null){
                bookingRequest.setRated(true);
                bookingRequestRepository.save(bookingRequest);
            }
        }

        User user = userRepository.findByEmail(request.getRevieweeEmail());
        user.setRating((user.getRating()* user.getRating_count()+ review.getRating())/ (user.getRating_count()+1));
        user.setRating_count(user.getRating_count()+1);
        userRepository.save(user);
        return review;
    }

//    public double getAverageRating(String revieweeEmail) {
////        return reviewRepository.avgReview(revieweeEmail);
//    }

    public boolean checkRated(String bookingId){
        BookingRequest bookingRequest = bookingRequestRepository.findById(new ObjectId(bookingId)).orElse(null);
        if(bookingRequest!=null) return bookingRequest.isRated();
        return false;
    }
}
