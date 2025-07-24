package com.example.carpooling.dto;

import com.example.carpooling.entities.Analytics;

import java.util.HashMap;
import java.util.Map;

public class AnalyticsDto {
    private long totalUsers;
    private long totalRides;
    private long totalBookings;
    private long totalSOS;

    private Map<String, Long> ridesPerDriver;
    private Map<String, Long> ridesByCity;

    public AnalyticsDto(Analytics analytics) {
        this.totalUsers = analytics.getTotalUsers();
        this.totalRides = analytics.getTotalRides();
        this.totalBookings = analytics.getTotalBookings();
        this.totalSOS = analytics.getTotalSOS();
        this.ridesByCity = analytics.getRidesByCity();
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

    public Map<String, Long> getRidesPerDriver() {
        return ridesPerDriver;
    }

    public void setRidesPerDriver(Map<String, Long> ridesPerDriver) {
        this.ridesPerDriver = ridesPerDriver;
    }

    public Map<String, Long> getRidesByCity() {
        return ridesByCity;
    }

    public void setRidesByCity(Map<String, Long> ridesByCity) {
        this.ridesByCity = ridesByCity;
    }
}
