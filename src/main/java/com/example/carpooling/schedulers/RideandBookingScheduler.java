package com.example.carpooling.schedulers;

import com.example.carpooling.dto.BookingWrapper;
import com.example.carpooling.entities.BookingRequest;
import com.example.carpooling.entities.Ride;
import com.example.carpooling.services.BookingRequestService;
import com.example.carpooling.services.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RideandBookingScheduler {

    @Autowired
    private BookingRequestService bookingRequestService;

    @Autowired
    private RideService rideService;

    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanup(){
        try {
            List<Ride> rides = rideService.getAllRides();

            for(Ride ride:rides){
                if(ride.getCreatedAt().isBefore(LocalDateTime.now().minusDays(30))){
                    rideService.deleteRide(ride.getId());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
