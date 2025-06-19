package com.example.carpooling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteMatchResult {
    private double score;
    private String reason;
}
