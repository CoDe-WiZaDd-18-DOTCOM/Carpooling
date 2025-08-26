package com.example.carpooling.dto;

import com.example.carpooling.entities.Ride;

public class SearchResponse {
    private Ride ride;
    private RouteMatchResult routeMatchResult;
    private String status;

    public SearchResponse(Ride ride,RouteMatchResult routeMatchResult,String status){
        this.ride=ride;
        this.routeMatchResult=routeMatchResult;
        this.status=status;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RouteMatchResult getRouteMatchResult() {
        return routeMatchResult;
    }

    public void setRouteMatchResult(RouteMatchResult routeMatchResult) {
        this.routeMatchResult = routeMatchResult;
    }
}
