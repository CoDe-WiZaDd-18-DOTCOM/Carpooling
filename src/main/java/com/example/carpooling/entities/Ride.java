package com.example.carpooling.entities;

import com.example.carpooling.enums.RideStatus;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "rides")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
public class Ride {

    @Id
    private ObjectId id;

    @Indexed
    @DBRef
    private User driver;

//    private RouteStop pickup;
//    private RouteStop drop;
    private List<RouteStop> route;

    private int seatCapacity;
    private int availableSeats;

    private Vehicle vehicle;
    private Preferences preferences;

    private RideStatus status;






    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }


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

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
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

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

}

