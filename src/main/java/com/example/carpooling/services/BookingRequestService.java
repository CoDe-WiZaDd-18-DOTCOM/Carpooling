package com.example.carpooling.services;

import com.example.carpooling.dto.BookingWrapper;
import com.example.carpooling.dto.SearchRequest;
import com.example.carpooling.entities.BookingRequest;
import com.example.carpooling.entities.Ride;
import com.example.carpooling.entities.User;
import com.example.carpooling.repositories.BookingRequestRepository;
import com.example.carpooling.repositories.RideRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingRequestService {
    @Autowired
    private BookingRequestRepository bookingRequestRepository;

    @Autowired
    private RideRepository rideRepository;

    public BookingRequest getBooking(ObjectId id) {
        return bookingRequestRepository.findById(id).orElse(null);
    }

    public List<BookingWrapper> getIncomingRequestsForDriver(User driver){
        List<BookingRequest> bookingRequests=bookingRequestRepository.findAllByDriver(driver);
        List<BookingWrapper> bookingWrappers = new ArrayList<>();

        for(BookingRequest bookingRequest:bookingRequests) bookingWrappers.add(new BookingWrapper(bookingRequest));
        return bookingWrappers;
    }

    public List<BookingWrapper> getUserRides(User rider){
        List<BookingRequest> bookingRequests=bookingRequestRepository.findAllByRider(rider);
        List<BookingWrapper> bookingWrappers = new ArrayList<>();

        for(BookingRequest bookingRequest:bookingRequests) bookingWrappers.add(new BookingWrapper(bookingRequest));
        return bookingWrappers;
    }

    public BookingRequest addRequest(SearchRequest searchRequest,Ride ride, User rider){
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setApproved(false);
        bookingRequest.setDestination(searchRequest.getDrop());
        bookingRequest.setPickup(searchRequest.getPickup());
        bookingRequest.setPreferredRoute(searchRequest.getPrefferedRoute());
        bookingRequest.setRide(ride);
        bookingRequest.setDriver(ride.getDriver());
        bookingRequest.setRider(rider);

        bookingRequestRepository.save(bookingRequest);
        return bookingRequest;
    }

    public BookingRequest approveRequest(BookingRequest bookingRequest){
        Ride ride = bookingRequest.getRide();

        if(ride.getAvailableSeats()==0) return null;

        ride.setAvailableSeats(ride.getAvailableSeats()-1);
        rideRepository.save(ride);
        bookingRequest.setRide(ride);
        bookingRequest.setApproved(true);
        bookingRequestRepository.save(bookingRequest);
        return bookingRequest;
    }

}
