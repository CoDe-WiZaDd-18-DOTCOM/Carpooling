package com.example.carpooling.controller;

import com.example.carpooling.dto.RideDto;
import com.example.carpooling.dto.SearchRequest;
import com.example.carpooling.dto.SearchResponse;
import com.example.carpooling.entities.Ride;
import com.example.carpooling.entities.User;
import com.example.carpooling.services.RideService;
import com.example.carpooling.services.UserService;
import com.example.carpooling.utils.AuthUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rides")
public class RideController {

    @Autowired
    private RideService rideService;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Ride>> getAllRides(){
        try {
            List<Ride> rides = rideService.getAllRides();
            return new ResponseEntity<>(rides,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<List<Ride>> getMyRides(){
        try {
            String email = authUtil.getEmail();
            List<Ride> rides = rideService.getAllRidesOfDriver(userService.getUser(email));
            return new ResponseEntity<>(rides,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/ride/{id}")
    public ResponseEntity<Ride> getRide(@PathVariable ObjectId id){
        try {
            Ride ride = rideService.getRide(id);
            return new ResponseEntity<>(ride,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<List<SearchResponse>> getPrefferedRides(@RequestBody SearchRequest searchRequest){
        try{
            User user = userService.getUser(authUtil.getEmail());
            return new ResponseEntity<>(rideService.getPrefferedRides(user,searchRequest),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<Ride> saveRide(@RequestBody RideDto rideDto){
        try{
            User user = userService.getUser(authUtil.getEmail());
            Ride ride=rideService.addRide(rideDto,user);
            return new ResponseEntity<>(ride,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
