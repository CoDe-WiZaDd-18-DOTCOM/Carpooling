package com.example.carpooling.services;

import com.example.carpooling.entities.Analytics;
import com.example.carpooling.repositories.AnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {
    @Autowired
    private AnalyticsRepository analyticsRepository;

    public Analytics getAnalytics(){
        return analyticsRepository.findById("default").orElse(null);
    }
}
