package com.example.carpooling.controller;

import com.example.carpooling.dto.*;
import com.example.carpooling.entities.Ride;
import com.example.carpooling.entities.User;
import com.example.carpooling.enums.Role;
import com.example.carpooling.services.RideService;
import com.example.carpooling.services.UserService;
import com.example.carpooling.utils.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    private static final Logger log = LoggerFactory.getLogger(RideController.class);

    @Operation(summary = "Get all rides", description = "Returns a list of all available rides in the system.")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RideWrapper>> getAllRides() {
        try {
            List<RideWrapper> rideWrappers = rideService.getAllRides();
            return new ResponseEntity<>(rideWrappers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<Page<RideWrapper>> getMyRides(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size) {
        try {
            User driver = userService.getUserById(authUtil.getId());
            Page<RideWrapper> rides = rideService.getAllRidesOfDriver(driver, page, size);
            return ResponseEntity.ok(rides);
        } catch (Exception e) {
            log.error(e.getMessage());
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
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Search preferred rides", description = "Returns a list of rides that match the user's preferences and search criteria such as route and time.")
    @PostMapping("/search")
    public ResponseEntity<List<SearchResponse>> getPrefferedRides(@RequestBody SearchRequest searchRequest) {
        try {
            User user = userService.getUserById(authUtil.getId());
            return new ResponseEntity<>(rideService.getPrefferedRides(user, searchRequest), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Create a new ride", description = "Allows a driver to create a new ride by providing route, time, and preferences. Requires DRIVER role.")
    @PostMapping
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<Ride> saveRide(@RequestBody RideDto rideDto) {
        try {
            User user = userService.getUserById(authUtil.getId());
            Ride ride = rideService.addRide(rideDto, user);
            return new ResponseEntity<>(ride, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Close a ride", description = "Allows a driver to close a completed ride by providing id. Requires DRIVER or ADMIN role.")
    @PostMapping("close-ride/{id}")
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN')")
    public ResponseEntity<Ride> closeRide(@PathVariable ObjectId id) {
        try {
            User user = userService.getUserById(authUtil.getId());
            Ride ride = rideService.closeRide(id);
            if(user.getRole().equals(Role.ADMIN)) log.info("admin closed the ride:" +id.toHexString());

            return new ResponseEntity<>(ride, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update a ride", description = "Allows a driver to update a ride by providing id and the info. Requires DRIVER or ADMIN role.")
    @PutMapping("/{rideId}")
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateRide(@PathVariable String rideId, @RequestBody UpdateRideDto updateDto) {
        try {
            Ride updatedRide = rideService.updateRide(rideId, updateDto);
            return ResponseEntity.ok(updatedRide);
        } catch (OptimisticLockingFailureException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Conflict: Ride was updated by someone else. Please reload and try again.", HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary = "Delete a ride", description = "Allows a driver to delete a ride by providing id. Requires DRIVER or ADMIN role.")
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteRide(@PathVariable ObjectId id) {
        try {
            User user = userService.getUserById(authUtil.getId());
            rideService.deleteRide(id);
            if(user.getRole().equals(Role.ADMIN)) log.info("admin deleted the ride:{}",id.toHexString());

            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
