package com.example.carpooling.dto;

import com.example.carpooling.entities.Preferences;
import com.example.carpooling.entities.RouteStop;
import com.example.carpooling.entities.Vehicle;
import com.example.carpooling.enums.RideStatus;

import java.util.List;

public class UpdateRideDto {
    private List<RouteStop> route;
    private int seatCapacity;
    private int availableSeats;
    private Vehicle vehicle;
    private Preferences preferences;
    private Long version;


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<RouteStop> getRoute() {
        return route;
    }

    public void setRoute(List<RouteStop> route) {
        this.route = route;
    }

    public int getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(int seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }
}
