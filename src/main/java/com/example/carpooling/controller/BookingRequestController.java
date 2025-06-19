package com.example.carpooling.controller;

import com.example.carpooling.dto.SearchRequest;
import com.example.carpooling.entities.BookingRequest;
import com.example.carpooling.entities.Ride;
import com.example.carpooling.entities.User;
import com.example.carpooling.services.BookingRequestService;
import com.example.carpooling.services.RideService;
import com.example.carpooling.services.UserService;
import com.example.carpooling.utils.AuthUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingRequestController {
    @Autowired
    private BookingRequestService bookingRequestService;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private RideService rideService;

    @GetMapping("/me")
    public ResponseEntity<List<BookingRequest>> getRiderBookings(){
        try {
            User rider = userService.getUser(authUtil.getEmail());
            return new ResponseEntity<>(bookingRequestService.getUserRides(rider), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/incoming")
    public ResponseEntity<List<BookingRequest>> getIncomingRequestsForDriver(){
        try {
            User rider = userService.getUser(authUtil.getEmail());
            return new ResponseEntity<>(bookingRequestService.getIncomingRequestsForDriver(rider), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/create-request")
    public ResponseEntity<BookingRequest> addRequest(@PathVariable ObjectId id,@RequestBody SearchRequest searchRequest){
        try {
            User rider = userService.getUser(authUtil.getEmail());
            Ride ride = rideService.getRide(id);

            BookingRequest bookingRequest = bookingRequestService.addRequest(searchRequest,ride,rider);
            return new ResponseEntity<>(bookingRequest,HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<BookingRequest> approveRequest(@PathVariable ObjectId id){
        try {
            BookingRequest booking = bookingRequestService.getBooking(id);
            User user = userService.getUser(authUtil.getEmail());

            if(!booking.getDriver().getId().equals(user.getId())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            BookingRequest bookingRequest = bookingRequestService.approveRequest(booking);
            if(bookingRequest==null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            return new ResponseEntity<>(bookingRequest,HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
