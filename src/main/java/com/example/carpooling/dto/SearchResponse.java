package com.example.carpooling.dto;

import com.example.carpooling.entities.Ride;

public class SearchResponse {
    private RideSearchDto ride;
    private RouteMatchResult routeMatchResult;
    private String status;

    public SearchResponse(Ride ride,RouteMatchResult routeMatchResult,String status){
        this.ride=new RideSearchDto(ride);
        this.routeMatchResult=routeMatchResult;
        this.status=status;
    }

    public RideSearchDto getRide() {
        return ride;
    }

    public void setRide(RideSearchDto ride) {
        this.ride = ride;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RideSearchDto getRideSearchDto() {
        return ride;
    }

    public void setRideSearchDto(RideSearchDto rideSearchDto) {
        this.ride = rideSearchDto;
    }

    public RouteMatchResult getRouteMatchResult() {
        return routeMatchResult;
    }

    public void setRouteMatchResult(RouteMatchResult routeMatchResult) {
        this.routeMatchResult = routeMatchResult;
    }
}
