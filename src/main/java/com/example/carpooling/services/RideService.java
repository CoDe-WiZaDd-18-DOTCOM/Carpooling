package com.example.carpooling.services;


import com.example.carpooling.dto.RideDto;
import com.example.carpooling.entities.Ride;
import com.example.carpooling.enums.RideStatus;
import com.example.carpooling.entities.User;
import com.example.carpooling.repositories.RideRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    public List<Ride> getAllRides(){
        return rideRepository.findAll();
    }

    public List<Ride> getAllRidesOfDriver(User driver){
        return rideRepository.findAllByDriver(driver);
    }

    public Ride getRide(ObjectId id){
        return rideRepository.findById(id).orElse(null);
    }

    public Ride addRide(RideDto rideDto,User user){
        Ride ride = new Ride();
        ride.setDriver(user);
        ride.setPickup(rideDto.getPickup());
        ride.setDrop(rideDto.getDrop());
        ride.setRoute(rideDto.getRoute());
        ride.setSeatCapacity(rideDto.getSeatCapacity());
        ride.setAvailableSeats(rideDto.getAvailableSeats());
        ride.setVehicle(rideDto.getVehicle());
        ride.setPreferences(rideDto.getPreferences());
        ride.setStatus(RideStatus.valueOf("OPEN"));

        rideRepository.save(ride);
        return ride;
    }
}
