package com.example.carpooling.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "analytics_summary")
public class Analytics {

    @Id
    private String id="default";

    private long totalUsers = 0;
    private long totalRides = 0;
    private long totalBookings = 0;
    private long totalSOS = 0;

    private Map<String, Long> ridesByCity = new HashMap<>();
    private Map<String, Long> ridesPerDriver = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalRides() {
        return totalRides;
    }

    public void setTotalRides(long totalRides) {
        this.totalRides = totalRides;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public long getTotalSOS() {
        return totalSOS;
    }

    public void setTotalSOS(long totalSOS) {
        this.totalSOS = totalSOS;
    }

    public Map<String, Long> getRidesByCity() {
        return ridesByCity;
    }

    public void setRidesByCity(Map<String, Long> ridesByCity) {
        this.ridesByCity = ridesByCity;
    }

    public Map<String, Long> getRidesPerDriver() {
        return ridesPerDriver;
    }

    public void setRidesPerDriver(Map<String, Long> ridesPerDriver) {
        this.ridesPerDriver = ridesPerDriver;
    }
}

