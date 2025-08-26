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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RideService {

    private static final Logger log = LoggerFactory.getLogger(RideService.class);
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

    public List<Ride> getAllRides(){
        return rideRepository.findAll();
    }

    public Page<Ride> getAllRidesOfDriver(User driver, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Ride> ridesPage = rideRepository.findAllByDriver(driver, pageable);
        return ridesPage;
    }

    public Ride getRide(String id){
        return rideRepository.findById(id).orElse(null);
    }

    public List<SearchResponse> getPrefferedRides(User user, SearchRequest searchRequest){
        List<Ride> rides=getCachedRides(searchRequest.getCity());
//        List<Ride> rides=rideRepository.findAllByStatusAndCity(RideStatus.OPEN,searchRequest.getCity());
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

    private List<Ride> getCachedRides(String city){
        List<Ride> rides=redisService.getList(city,Ride.class);
        redisService.incrementCityCount(city);

        if(rides==null){
            log.info("rides cache miss");
            rides=rideRepository.findAllByStatusAndCity(RideStatus.OPEN,city);
            Set<String> topCities=redisService.getTop5Cities();
            if(topCities.size()<5 || topCities.contains(city)){
                redisService.set(city, rides,300L);
                log.info("current city rides are cached");
            }

            if(topCities.contains(city) && redisService.getLength()>5){
                List<String> toRemove=redisService.getToRemoveCity();
                redisService.delete(toRemove.get(0));
                log.info("evicted the last lfu city cache");
            }
        }
        return rides;
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
        ride.setCity(rideDto.getCity());


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

    public Ride closeRide(String id){
        Ride ride = rideRepository.findById(id).orElse(null);
        if(ride!=null) {
            ride.setStatus(RideStatus.CLOSED);
            ride.setCompletedAt(LocalDateTime.now());
            rideRepository.save(ride);
            redisService.delete(ride.getCity());
        }
        return ride;
    }

    public Ride updateRide(String rideId, RideDto dto) {
        Ride ride = rideRepository.findById(rideId).orElse(null);
        if(ride==null) return null;
        ride.setRoute(dto.getRoute());
        ride.setSeatCapacity(dto.getSeatCapacity());
        ride.setAvailableSeats(dto.getAvailableSeats());
        ride.setVehicle(dto.getVehicle());
        ride.setPreferences(dto.getPreferences());
        ride.setVersion(dto.getVersion());
        redisService.delete(ride.getCity());

        return rideRepository.save(ride);
    }


    public void deleteRide(String id){
        try {
            Ride ride=rideRepository.findById(id).orElse(null);
            if(ride==null) return;

            List<BookingRequest> bookingRequests = bookingRequestRepository.findAllByRide(ride);
            for(BookingRequest bookingRequest:bookingRequests) bookingRequestRepository.delete(bookingRequest);
            redisService.delete(ride.getCity());
            rideRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
