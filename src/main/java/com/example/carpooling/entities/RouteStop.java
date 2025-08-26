package com.example.carpooling.entities;

import java.time.LocalTime;

public class RouteStop {
    private Location location;
    private LocalTime arrivalTime;

    public RouteStop() {
    }

    public RouteStop(Location location, LocalTime arrivalTime) {
        this.location = location;
        this.arrivalTime = arrivalTime;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}

