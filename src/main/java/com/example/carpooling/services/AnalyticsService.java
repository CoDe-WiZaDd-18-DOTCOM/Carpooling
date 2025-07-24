package com.example.carpooling.services;

import com.example.carpooling.dto.AnalyticsDto;
import com.example.carpooling.entities.Analytics;
import com.example.carpooling.entities.Location;
import com.example.carpooling.repositories.AnalyticsRepository;
import com.example.carpooling.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyticsService {
    @Autowired
    private AnalyticsRepository analyticsRepository;

    @Autowired
    private UserRepository userRepository;

    public Analytics getAnalytics() {
        Analytics analytics = analyticsRepository.findById("default").orElse(null);
        if (analytics == null) {
            analytics = new Analytics();
            analyticsRepository.save(analytics);
        }
        return analytics;
    }

    public AnalyticsDto get(){
        Analytics analytics=getAnalytics();

        AnalyticsDto analyticsDto = new AnalyticsDto(analytics);

        Map<String,Long> rides = analytics.getRidesPerDriver();
        Map<String,Long> newrides = new HashMap<>();

        rides.forEach((key,value)->
                newrides.put(userRepository.findById(new ObjectId(key)).orElse(null).getEmail(),value));

        analyticsDto.setRidesPerDriver(newrides);
        return analyticsDto;
    }

    public void incUsers(){
        Analytics analytics=getAnalytics();
        analytics.setTotalUsers(analytics.getTotalUsers()+1);
        analyticsRepository.save(analytics);
    }

    public void incRides(String id, String city){
        Analytics analytics=getAnalytics();
        analytics.setTotalRides(analytics.getTotalRides()+1);

        Map<String, Long> ridesPerDriver = analytics.getRidesPerDriver();
        ridesPerDriver.put(id,ridesPerDriver.getOrDefault(id,0l)+1);
        analytics.setRidesPerDriver(ridesPerDriver);

        Map<String, Long> ridesByCity = analytics.getRidesByCity();
        ridesByCity.put(city,ridesByCity.getOrDefault(city,0l)+1);
        analytics.setRidesByCity(ridesByCity);

        analyticsRepository.save(analytics);
    }

    public void incBookings(){
        Analytics analytics=getAnalytics();
        analytics.setTotalBookings(analytics.getTotalBookings()+1);
        analyticsRepository.save(analytics);
    }

    public void incSos(){
        Analytics analytics=getAnalytics();
        analytics.setTotalSOS(analytics.getTotalSOS()+1);
        analyticsRepository.save(analytics);
    }

}
