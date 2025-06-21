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

    public SosAlerts addAlert(User user,String message, BookingRequest bookingRequest){
        SosAlerts sosAlerts = new SosAlerts(message, user, bookingRequest);
        sosAlertsRepository.save(sosAlerts);
        return sosAlerts;
    }

    public List<SosAlerts> getAlerts(){
        return sosAlertsRepository.findAll();
    }
}
