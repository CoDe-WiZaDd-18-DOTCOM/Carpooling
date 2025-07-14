package com.example.carpooling.entities;

import com.example.carpooling.enums.SosStatus;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sos_alerts")
public class SosAlerts {

    @Id
    private ObjectId id;

    private String message;


    @DBRef
    private BookingRequest bookingRequest;

    private SosStatus status;

    public SosAlerts(String message, BookingRequest bookingRequest) {
        this.message = message;
        this.bookingRequest = bookingRequest;
        status=SosStatus.PENDING;
    }

    public SosStatus getStatus() {
        return status;
    }

    public void setStatus(SosStatus status) {
        this.status = status;
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


    public BookingRequest getBookingRequest() {
        return bookingRequest;
    }

    public void setBookingRequest(BookingRequest bookingRequest) {
        this.bookingRequest = bookingRequest;
    }
}
