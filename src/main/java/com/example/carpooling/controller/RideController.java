package com.example.carpooling.controller;

import com.example.carpooling.dto.RideDto;
import com.example.carpooling.dto.RideWrapper;
import com.example.carpooling.dto.SearchRequest;
import com.example.carpooling.dto.SearchResponse;
import com.example.carpooling.entities.Ride;
import com.example.carpooling.entities.User;
import com.example.carpooling.services.RideService;
import com.example.carpooling.services.UserService;
import com.example.carpooling.utils.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rides")
@Tag(name = "Rides", description = "APIs for creating, listing, and searching carpooling rides.")
public class RideController {

    @Autowired
    private RideService rideService;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all rides", description = "Returns a list of all available rides in the system.")
    @GetMapping
    public ResponseEntity<List<Ride>> getAllRides() {
        try {
            List<Ride> rides = rideService.getAllRides();
            return new ResponseEntity<>(rides, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get rides created by current driver", description = "Returns all rides posted by the currently authenticated driver. Requires DRIVER role.")
    @GetMapping("/me")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<List<RideWrapper>> getMyRides() {
        try {
            String email = authUtil.getEmail();
            List<RideWrapper> rides = rideService.getAllRidesOfDriver(userService.getUser(email));
            return new ResponseEntity<>(rides, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get ride by ID", description = "Returns details of a specific ride based on its ID.")
    @GetMapping("/ride/{id}")
    public ResponseEntity<Ride> getRide(@PathVariable ObjectId id) {
        try {
            Ride ride = rideService.getRide(id);
            return new ResponseEntity<>(ride, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Search preferred rides", description = "Returns a list of rides that match the user's preferences and search criteria such as route and time.")
    @PostMapping("/search")
    public ResponseEntity<List<SearchResponse>> getPrefferedRides(@RequestBody SearchRequest searchRequest) {
        try {
            User user = userService.getUser(authUtil.getEmail());
            return new ResponseEntity<>(rideService.getPrefferedRides(user, searchRequest), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Create a new ride", description = "Allows a driver to create a new ride by providing route, time, and preferences. Requires DRIVER role.")
    @PostMapping
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<Ride> saveRide(@RequestBody RideDto rideDto) {
        try {
            User user = userService.getUser(authUtil.getEmail());
            Ride ride = rideService.addRide(rideDto, user);
            return new ResponseEntity<>(ride, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Close a ride", description = "Allows a driver to close a completed ride by providing id. Requires DRIVER role.")
    @PostMapping("close-ride/{id}")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<Ride> closeRide(@PathVariable ObjectId id) {
        try {
            Ride ride = rideService.closeRide(id);
            return new ResponseEntity<>(ride, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
