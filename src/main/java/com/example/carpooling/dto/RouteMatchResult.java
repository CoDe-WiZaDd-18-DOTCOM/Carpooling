package com.example.carpooling.dto;

import java.time.LocalTime;

//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//@Data
//@AllArgsConstructor
public class RouteMatchResult {
    private double score;
    private String reason;
    private LocalTime arraivalTime;

    public RouteMatchResult(double score,String reason,LocalTime arraivalTime){
        this.score=score;
        this.reason=reason;
        this.arraivalTime=arraivalTime;
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
