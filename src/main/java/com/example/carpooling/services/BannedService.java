package com.example.carpooling.services;

import com.example.carpooling.entities.Banned;
import com.example.carpooling.repositories.BannedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannedService {
    @Autowired
    private BannedRepository bannedRepository;

    public boolean existByEmail(String email){
        return bannedRepository.existsByEmail(email);
    }

    public Banned findByEmail(String email){
        return bannedRepository.findByEmail(email);
    }

    public List<Banned> findAll(){
        return bannedRepository.findAll();
    }

    public void liftBan(String email){
        bannedRepository.deleteByEmail(email);
    }
}
