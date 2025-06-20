package com.example.carpooling.dto;

import com.example.carpooling.entities.BookingRequest;

public class BookingWrapper {
    private String id;
    private BookingRequest bookingRequest;

    public BookingWrapper(BookingRequest bookingRequest){
        this.id=bookingRequest.getId().toHexString();
        this.bookingRequest=bookingRequest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BookingRequest getBookingRequest() {
        return bookingRequest;
    }

    public void setBookingRequest(BookingRequest bookingRequest) {
        this.bookingRequest = bookingRequest;
    }
}
