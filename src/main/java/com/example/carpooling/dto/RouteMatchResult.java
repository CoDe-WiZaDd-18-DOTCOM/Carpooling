package com.example.carpooling.dto;

//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//@Data
//@AllArgsConstructor
public class RouteMatchResult {
    private double score;
    private String reason;

    public RouteMatchResult(double score,String reason){
        this.score=score;
        this.reason=reason;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
