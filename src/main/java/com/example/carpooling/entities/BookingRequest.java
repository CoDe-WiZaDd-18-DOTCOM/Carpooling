package com.example.carpooling.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@CompoundIndexes({
        @CompoundIndex(name = "unique_rider_ride", def = "{'rider': 1, 'ride': 1}", unique = true)
})
@Document(collection = "booking_requests")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
public class BookingRequest {

    @Id
    private ObjectId id;

    @DBRef
    private Ride ride;

    @DBRef
    private User rider;

    @DBRef
    private User driver;

    private Location pickup;

    private Location destination;

    private List<Location> preferredRoute;

    private boolean isApproved;



    public Location getPickup() {
        return pickup;
    }

    public void setPickup(Location pickup) {
        this.pickup = pickup;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public List<Location> getPreferredRoute() {
        return preferredRoute;
    }

    public void setPreferredRoute(List<Location> preferredRoute) {
        this.preferredRoute = preferredRoute;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public User getRider() {
        return rider;
    }

    public void setRider(User rider) {
        this.rider = rider;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}

