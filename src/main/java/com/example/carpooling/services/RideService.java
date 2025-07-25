package com.example.carpooling.services;


import com.example.carpooling.dto.*;
import com.example.carpooling.entities.BookingRequest;
import com.example.carpooling.entities.Ride;
import com.example.carpooling.enums.RideStatus;
import com.example.carpooling.entities.User;
import com.example.carpooling.repositories.BookingRequestRepository;
import com.example.carpooling.repositories.RideRepository;
import com.example.carpooling.utils.RouteComparisonUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private RouteComparisonUtil routeComparisonUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private BookingRequestRepository bookingRequestRepository;

    @Autowired
    private AnalyticsService analyticsService;

    public List<RideWrapper> getAllRides(){
        List<Ride> rides= rideRepository.findAll();
        List<RideWrapper> rideWrappers = new ArrayList<>();
        for(Ride ride:rides){
            rideWrappers.add(new RideWrapper(ride));
        }
        return rideWrappers;
    }

    public Page<RideWrapper> getAllRidesOfDriver(User driver, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Ride> ridesPage = rideRepository.findAllByDriver(driver, pageable);
        return ridesPage.map(RideWrapper::new);
    }

    public Ride getRide(ObjectId id){
        return rideRepository.findById(id).orElse(null);
    }

    public List<SearchResponse> getPrefferedRides(User user, SearchRequest searchRequest){
        List<Ride> rides = rideRepository.findAllByStatus(RideStatus.OPEN);

        List<SearchResponse> searchResponses = new ArrayList<>();
        for(Ride ride:rides){
            RouteMatchResult routeMatchResult = routeComparisonUtil.compareRoute(ride.getRoute(),
                                                                            searchRequest.getPreferredRoute(),
                    searchRequest.getPickup(), searchRequest.getDrop(), ride.getPreferences(), user.getPreferences());
            if(routeMatchResult.getScore()!=0) {
                BookingRequest bookingRequest = checkAndGetRequest(ride, user);
                String status="NULL";
                if(bookingRequest!=null) {
                    if(bookingRequest.isApproved()) status="APPROVED";
                    else status="PENDING";
                }
                searchResponses.add(new SearchResponse(ride,routeMatchResult, status));
            }
        }
        searchResponses.sort((a, b) -> Double.compare(b.getRouteMatchResult().getScore(), a.getRouteMatchResult().getScore()));
        return searchResponses;
    }

    public Ride addRide(RideDto rideDto,User user){
        Ride ride = new Ride();
        ride.setDriver(user);
        ride.setRoute(rideDto.getRoute());
        ride.setSeatCapacity(rideDto.getSeatCapacity());
        ride.setAvailableSeats(rideDto.getAvailableSeats());
        ride.setVehicle(rideDto.getVehicle());
        ride.setPreferences(rideDto.getPreferences());
        ride.setStatus(RideStatus.valueOf("OPEN"));
        ride.setCreatedAt(LocalDateTime.now());


        rideRepository.save(ride);
        analyticsService.incRides(user.getId().toHexString(), rideDto.getCity());
        return ride;
    }

    public BookingRequest checkAndGetRequest(Ride ride, User rider){
        if(bookingRequestRepository.existsByRideAndRider(ride,rider)){
            return bookingRequestRepository.findByRideAndRider(ride,rider);
        }
        return null;
    }

    public Ride closeRide(ObjectId id){
        Ride ride = rideRepository.findById(id).orElse(null);
        if(ride!=null) {
            ride.setStatus(RideStatus.CLOSED);
            ride.setCompletedAt(LocalDateTime.now());
            rideRepository.save(ride);
        }
        return ride;
    }

    public Ride updateRide(String rideId, UpdateRideDto dto) {
        ObjectId objectId= new ObjectId(rideId);

        Ride ride = rideRepository.findById(objectId).orElse(null);
        if(ride==null) return null;
        ride.setRoute(dto.getRoute());
        ride.setSeatCapacity(dto.getSeatCapacity());
        ride.setAvailableSeats(dto.getAvailableSeats());
        ride.setVehicle(dto.getVehicle());
        ride.setPreferences(dto.getPreferences());
        ride.setVersion(dto.getVersion());

        return rideRepository.save(ride);
    }


    public void deleteRide(ObjectId id){
        try {
            Ride ride=rideRepository.findById(id).orElse(null);
            if(ride==null) return;

            List<BookingRequest> bookingRequests = bookingRequestRepository.findAllByRide(ride);
            for(BookingRequest bookingRequest:bookingRequests) bookingRequestRepository.delete(bookingRequest);
            rideRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
