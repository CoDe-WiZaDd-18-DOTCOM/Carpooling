package com.example.carpooling.dto;

import org.springframework.data.annotation.Id;

public class AvgRatingResult {
    @Id
    private String id;

    private double avgrating;  // Must match aggregation field

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAvgrating() {
        return avgrating;
    }

    public void setAvgrating(double avgrating) {
        this.avgrating = avgrating;
    }

    @Override
    public String toString() {
        return "AvgRatingResult{id='" + id + "', avgrating=" + avgrating + "}";
    }
}



