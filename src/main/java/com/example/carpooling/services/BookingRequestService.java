package com.example.carpooling.services;

import com.example.carpooling.dto.BookingWrapper;
import com.example.carpooling.dto.SearchRequest;
import com.example.carpooling.entities.BookingRequest;
import com.example.carpooling.entities.Ride;
import com.example.carpooling.entities.User;
import com.example.carpooling.enums.RideStatus;
import com.example.carpooling.repositories.BookingRequestRepository;
import com.example.carpooling.repositories.RideRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class BookingRequestService {
    @Autowired
    private BookingRequestRepository bookingRequestRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AnalyticsService analyticsService;

    public BookingRequest getBooking(String id) {
        return bookingRequestRepository.findById(id).orElse(null);
    }

    public List<BookingRequest> getIncomingRequestsForDriver(User driver){
        return bookingRequestRepository.findAllByDriver(driver);
    }


    public Page<BookingRequest> getUserRides(User rider, int page, int size) {
        Sort sort = Sort.by("pickup.arrivalTime").ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BookingRequest> bookingPage = bookingRequestRepository.findAllByRider(rider, pageable);
        return bookingPage;
    }



    public BookingRequest addRequest(SearchRequest searchRequest,Ride ride, User rider){
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setApproved(false);
        bookingRequest.setDestination(searchRequest.getDrop());
        bookingRequest.setPickup(searchRequest.getPickup());
        bookingRequest.setPreferredRoute(searchRequest.getPreferredRoute());
        bookingRequest.setRide(ride);
        bookingRequest.setDriver(ride.getDriver());
        bookingRequest.setRider(rider);
        bookingRequest.setRated(false);

        bookingRequestRepository.save(bookingRequest);
        return bookingRequest;
    }

    public BookingRequest approveRequest(BookingRequest bookingRequest){
        Ride ride = bookingRequest.getRide();

        if(ride.getAvailableSeats()==0) {
            ride.setStatus(RideStatus.FILLED);
            rideRepository.save(ride);
            bookingRequest.setRide(ride);
            bookingRequest.setApproved(false);
            bookingRequestRepository.save(bookingRequest);
            redisService.delete("rides");
            return null;
        }

        ride.setAvailableSeats(ride.getAvailableSeats()-1);
        rideRepository.save(ride);
        bookingRequest.setRide(ride);
        bookingRequest.setApproved(true);
        bookingRequestRepository.save(bookingRequest);
        analyticsService.incBookings();
        return bookingRequest;
    }

    public List<BookingRequest> getBookingByRide(Ride ride){
        return bookingRequestRepository.findAllByRide(ride);
    }

    public void deleteRequest(String id){
        try {
            bookingRequestRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
