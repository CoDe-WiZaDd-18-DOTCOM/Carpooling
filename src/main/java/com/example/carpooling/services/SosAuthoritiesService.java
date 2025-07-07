package com.example.carpooling.services;

import com.example.carpooling.dto.SosAuthorityMapper;
import com.example.carpooling.entities.SosAuthorities;
import com.example.carpooling.repositories.SosAuthoritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class SosAuthoritiesService {
    @Autowired
    private SosAuthoritesRepository sosAuthoritesRepository;

    public SosAuthorities addAuthority(SosAuthorityMapper sosAuthorityMapper){
        SosAuthorities sosAuthorities = new SosAuthorities(sosAuthorityMapper.getLabel(), sosAuthorityMapper.getEmail());
        sosAuthoritesRepository.save(sosAuthorities);
        return sosAuthorities;
    }

    public String getEmail(String label){
        return sosAuthoritesRepository.findByLabel(label).getEmail();
    }
}
