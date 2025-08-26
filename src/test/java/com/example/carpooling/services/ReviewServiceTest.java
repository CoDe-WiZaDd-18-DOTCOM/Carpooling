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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

//    @Mock
//    private ReviewRepository reviewRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private BookingRequestRepository bookingRequestRepository;
//
//    @Mock
//    private AuthUtil authUtil;
//
//    @InjectMocks
//    private ReviewService reviewService;
//
//    private User reviewee;
//    private User reviewer;
//    private BookingRequest bookingRequest;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        reviewer = new User();
//        reviewer.setId(new ObjectId());
//        reviewer.setEmail("reviewer@example.com");
//
//        reviewee = new User();
//        reviewee.setId(new ObjectId());
//        reviewee.setEmail("reviewee@example.com");
//        reviewee.setRating(4.0);
//        reviewee.setRating_count(1);
//
//        bookingRequest = new BookingRequest();
//        bookingRequest.setId(new ObjectId());
//        bookingRequest.setRated(false);
//    }
//
//    @Test
//    void submitReview_ShouldSaveReview_UpdateBookingAndUserRating() {
//        ReviewDto dto = new ReviewDto();
//        dto.setRevieweeEmail(reviewee.getEmail());
//        dto.setRating(5);
//        dto.setComment("Great ride!");
//        dto.setBookingId(bookingRequest.getId().toHexString());
//
//        when(authUtil.getId()).thenReturn(reviewer.getId().toHexString());
//        when(userRepository.findById(reviewer.getId())).thenReturn(Optional.of(reviewer));
//        when(bookingRequestRepository.findById(bookingRequest.getId())).thenReturn(Optional.of(bookingRequest));
//        when(userRepository.findByEmail(reviewee.getEmail())).thenReturn(reviewee);
//        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);
//        when(bookingRequestRepository.save(any(BookingRequest.class))).thenAnswer(i -> i.getArguments()[0]);
//        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
//
//        Review review = reviewService.submitReview(dto);
//
//        // Review fields
//        assertEquals("reviewer@example.com", review.getReviewer());
//        assertEquals("reviewee@example.com", review.getReviewee());
//        assertEquals(5, review.getRating());
//        assertEquals("Great ride!", review.getComment());
//        assertNotNull(review.getCreatedAt());
//
//        // BookingRequest marked rated
//        assertTrue(bookingRequest.isRated());
//        verify(bookingRequestRepository, times(1)).save(bookingRequest);
//
//        // User rating updated correctly
//        assertEquals(4.5, reviewee.getRating()); // (4*1 + 5)/2 = 4.5
//        assertEquals(2, reviewee.getRating_count());
//        verify(userRepository, times(1)).save(reviewee);
//
//        // Verify review saved
//        verify(reviewRepository, times(1)).save(review);
//    }
//
//    @Test
//    void checkRated_ShouldReturnTrue_WhenBookingIsRated() {
//        bookingRequest.setRated(true);
//        when(bookingRequestRepository.findById(bookingRequest.getId())).thenReturn(Optional.of(bookingRequest));
//
//        boolean rated = reviewService.checkRated(bookingRequest.getId().toHexString());
//
//        assertTrue(rated);
//    }
//
//    @Test
//    void checkRated_ShouldReturnFalse_WhenBookingNotFound() {
//        when(bookingRequestRepository.findById(any(ObjectId.class))).thenReturn(Optional.empty());
//
//        boolean rated = reviewService.checkRated(new ObjectId().toHexString());
//
//        assertFalse(rated);
//    }
//
//    @Test
//    void checkRated_ShouldReturnFalse_WhenBookingNotRated() {
//        bookingRequest.setRated(false);
//        when(bookingRequestRepository.findById(bookingRequest.getId())).thenReturn(Optional.of(bookingRequest));
//
//        boolean rated = reviewService.checkRated(bookingRequest.getId().toHexString());
//
//        assertFalse(rated);
//    }
}
