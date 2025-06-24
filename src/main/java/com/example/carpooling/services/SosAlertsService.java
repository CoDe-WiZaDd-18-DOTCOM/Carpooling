package com.example.carpooling.services;

import com.example.carpooling.entities.BookingRequest;
import com.example.carpooling.entities.SosAlerts;
import com.example.carpooling.entities.User;
import com.example.carpooling.repositories.SosAlertsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SosAlertsService {

    @Autowired
    private SosAlertsRepository sosAlertsRepository;

    @Autowired
    private RedisService redisService;

    public SosAlerts addAlert(String message, BookingRequest bookingRequest){
        SosAlerts sosAlerts = new SosAlerts(message, bookingRequest);
        sosAlertsRepository.save(sosAlerts);
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
        redisService.set("sosAlerts",sosAlerts,3*60*60l);
        return sosAlertsRepository.findAll();
    }
}
