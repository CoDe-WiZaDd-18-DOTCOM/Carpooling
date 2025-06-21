package com.example.carpooling.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sos_alerts")
public class SosAlerts {

    @Id
    private ObjectId id;

    private String message;

    @DBRef
    private User user;

    @DBRef
    private BookingRequest bookingRequest;

    public SosAlerts(String message, User user, BookingRequest bookingRequest) {
        this.message = message;
        this.user = user;
        this.bookingRequest = bookingRequest;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BookingRequest getBookingRequest() {
        return bookingRequest;
    }

    public void setBookingRequest(BookingRequest bookingRequest) {
        this.bookingRequest = bookingRequest;
    }
}
