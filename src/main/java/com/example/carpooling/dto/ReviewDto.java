package com.example.carpooling.dto;

public class ReviewDto {
//    private String reviewerEmail;
    private String revieweeEmail;
    private int rating;
    private String comment;
    private String bookingId;



//    public String getReviewerEmail() {
//        return reviewerEmail;
//    }
//
//    public void setReviewerEmail(String reviewerEmail) {
//        this.reviewerEmail = reviewerEmail;
//    }


    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getRevieweeEmail() {
        return revieweeEmail;
    }

    public void setRevieweeEmail(String revieweeEmail) {
        this.revieweeEmail = revieweeEmail;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
