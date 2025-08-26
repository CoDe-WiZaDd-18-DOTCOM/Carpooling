package com.example.carpooling.services;

import com.example.carpooling.dto.SosAlertWrapper;
import com.example.carpooling.entities.BookingRequest;
import com.example.carpooling.entities.SosAlerts;
import com.example.carpooling.entities.User;
import com.example.carpooling.enums.SosStatus;
import com.example.carpooling.repositories.SosAlertsRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SosAlertsService {

    @Autowired
    private SosAlertsRepository sosAlertsRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AnalyticsService analyticsService;

    private static final Logger logger = LoggerFactory.getLogger(SosAlertsService.class);

    public SosAlerts addAlert(String message, BookingRequest bookingRequest){
        SosAlerts sosAlerts = new SosAlerts(message, bookingRequest);
        sosAlertsRepository.save(sosAlerts);
        analyticsService.incSos();

        redisService.delete("sosAlerts");
        return sosAlerts;
    }

    public List<SosAlerts> getAlerts(){
        List<SosAlerts> sosAlerts = redisService.getList("sosAlerts",SosAlerts.class);
        if(sosAlerts!=null){
            System.out.println("cache hit on sosalerts");
            return sosAlerts;
        }

        sosAlerts=sosAlertsRepository.findAll();

        redisService.set("sosAlerts",sosAlerts,300L);
        return sosAlerts;
    }

    public void closeAlert(String id){
        SosAlerts sosAlerts = sosAlertsRepository.findById(id).orElse(null);
        if(sosAlerts==null) return;
        sosAlerts.setStatus(SosStatus.SOLVED);
        sosAlertsRepository.save(sosAlerts);
    }
}
