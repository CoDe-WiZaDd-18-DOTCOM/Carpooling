package com.example.carpooling.services;

import com.example.carpooling.dto.SosAuthorityMapper;
import com.example.carpooling.entities.SosAuthorities;
import com.example.carpooling.repositories.SosAuthoritesRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SosAuthoritiesService {
    @Autowired
    private SosAuthoritesRepository sosAuthoritesRepository;

    public SosAuthorities addAuthority(SosAuthorityMapper sosAuthorityMapper){
        SosAuthorities sosAuthorities = new SosAuthorities(sosAuthorityMapper.getLabel(), sosAuthorityMapper.getEmail());
        sosAuthoritesRepository.save(sosAuthorities);
        return sosAuthorities;
    }

    public SosAuthorities getAuthority(String label){
        return sosAuthoritesRepository.findByLabel(label);
    }

    public SosAuthorities updateAuthority(ObjectId objectId,SosAuthorityMapper sosAuthorityMapper){
        SosAuthorities sosAuthorities = sosAuthoritesRepository.findById(objectId).orElse(null);
        if(sosAuthorities.getEmail()!=null && !Objects.equals(sosAuthorities.getEmail(), ""))
            sosAuthorities.setEmail(sosAuthorityMapper.getEmail());

        sosAuthoritesRepository.save(sosAuthorities);
        return sosAuthorities;
    }
}
