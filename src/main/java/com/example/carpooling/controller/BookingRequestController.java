package com.example.carpooling.controller;

import com.example.carpooling.dto.BookingWrapper;
import com.example.carpooling.dto.RideWrapper;
import com.example.carpooling.dto.SearchRequest;
import com.example.carpooling.entities.BookingRequest;
import com.example.carpooling.entities.Ride;
import com.example.carpooling.entities.User;
import com.example.carpooling.services.BookingRequestService;
import com.example.carpooling.services.RedisService;
import com.example.carpooling.services.RideService;
import com.example.carpooling.services.UserService;
import com.example.carpooling.utils.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@Tag(name = "Bookings", description = "Endpoints for managing ride booking requests between riders and drivers.")
public class BookingRequestController {

    private static final Logger log = LoggerFactory.getLogger(BookingRequestController.class);
    @Autowired
    private BookingRequestService bookingRequestService;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private RideService rideService;


    @Operation(summary = "Get booking by ID", description = "Returns a single booking request given its unique ID.")
    @GetMapping("/booking/{id}")
    public ResponseEntity<BookingRequest> getBookingById(@PathVariable ObjectId id) {
        try {
            BookingRequest bookingRequest = bookingRequestService.getBooking(id);
            if (bookingRequest == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(bookingRequest, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get bookings for a ride", description = "Returns all booking requests associated with a specific ride ID.")
    @GetMapping("/booking/by-ride/{id}")
    public ResponseEntity<List<BookingWrapper>> getBookingByRide(@PathVariable ObjectId id) {
        try {
            List<BookingWrapper> bookingWrapper = bookingRequestService.getBookingByRide(rideService.getRide(id));
            return new ResponseEntity<>(bookingWrapper, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get bookings by current user", description = "Returns all ride bookings made by the authenticated user (rider).")
    @GetMapping("/me")
    public ResponseEntity<List<BookingWrapper>> getRiderBookings() {
        try {
            User rider = userService.getUser(authUtil.getEmail());
            return new ResponseEntity<>(bookingRequestService.getUserRides(rider), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get incoming booking requests", description = "Returns all pending booking requests for rides owned by the current user (driver). Requires DRIVER role.")
    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/incoming")
    public ResponseEntity<List<BookingWrapper>> getIncomingRequestsForDriver() {
        try {
            User rider = userService.getUser(authUtil.getEmail());
            return new ResponseEntity<>(bookingRequestService.getIncomingRequestsForDriver(rider), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Create a booking request for a ride", description = "Creates a new booking request from the authenticated user (rider) for a given ride ID using provided search parameters.")
    @PostMapping("/{id}/create-request")
    public ResponseEntity<BookingRequest> addRequest(@PathVariable ObjectId id, @RequestBody SearchRequest searchRequest) {
        try {
            User rider = userService.getUser(authUtil.getEmail());
            Ride ride = rideService.getRide(id);

            BookingRequest bookingRequest = bookingRequestService.addRequest(searchRequest, ride, rider);
            return new ResponseEntity<>(bookingRequest, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Approve a booking request", description = "Allows a driver to approve a booking request by ID. Requires DRIVER role and the driver must own the ride.")
    @PreAuthorize("hasRole('DRIVER')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<BookingRequest> approveRequest(@PathVariable ObjectId id) {
        try {
            BookingRequest booking = bookingRequestService.getBooking(id);
            User user = userService.getUser(authUtil.getEmail());

            if (!booking.getDriver().getId().equals(user.getId())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            BookingRequest bookingRequest = bookingRequestService.approveRequest(booking);
            if (bookingRequest == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            return new ResponseEntity<>(bookingRequest, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Delete a booking request", description = "Allows a rider to delete a booking request by ID.Rider must own that booking request and it should not be accepted yet.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRequest(@PathVariable ObjectId id) {
        try {
            BookingRequest booking = bookingRequestService.getBooking(id);

            if (!booking.getRider().getEmail().equals(authUtil.getEmail()) || booking.isApproved()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            bookingRequestService.deleteRequest(id);
            return new ResponseEntity<>("deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
