package com.example.carpooling.dto;

import com.example.carpooling.entities.Preferences;
import com.example.carpooling.entities.RouteStop;
import com.example.carpooling.entities.Vehicle;

import java.util.List;

public class RideDto {
//    private RouteStop pickup;
//    private RouteStop drop;
    private List<RouteStop> route;

    private int seatCapacity;
    private int availableSeats;

    private Vehicle vehicle;
    private Preferences preferences;

//    public RouteStop getPickup() {
//        return pickup;
//    }
//
//    public void setPickup(RouteStop pickup) {
//        this.pickup = pickup;
//    }
//
//    public RouteStop getDrop() {
//        return drop;
//    }
//
//    public void setDrop(RouteStop drop) {
//        this.drop = drop;
//    }

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
