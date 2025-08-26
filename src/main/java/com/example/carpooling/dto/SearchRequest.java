package com.example.carpooling.dto;

import com.example.carpooling.entities.Location;

import java.util.List;

public class SearchRequest {
    private Location pickup;
    private Location drop;
    private List<Location> preferredRoute;
    private String city;

    public Location getPickup() {
        return pickup;
    }

    public void setPickup(Location pickup) {
        this.pickup = pickup;
    }

    public Location getDrop() {
        return drop;
    }

    public void setDrop(Location drop) {
        this.drop = drop;
    }

    public List<Location> getPreferredRoute() {
        return preferredRoute;
    }

    public void setPreferredRoute(List<Location> preferredRoute) {
        this.preferredRoute = preferredRoute;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
