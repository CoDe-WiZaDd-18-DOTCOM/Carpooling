package com.example.carpooling.dto;

import com.example.carpooling.entities.Ride;

public class SearchResponse {
    private Ride ride;
    private RouteMatchResult routeMatchResult;

    public SearchResponse(Ride ride,RouteMatchResult routeMatchResult){
        this.ride=ride;
        this.routeMatchResult=routeMatchResult;
    }


    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public RouteMatchResult getRouteMatchResult() {
        return routeMatchResult;
    }

    public void setRouteMatchResult(RouteMatchResult routeMatchResult) {
        this.routeMatchResult = routeMatchResult;
    }
}
