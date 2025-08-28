package com.example.carpooling.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateLimiter {
    private static final Long DURATION=1L;
    private static final Long RATE_PER_DURATION=100L;

    @Autowired
    private RedisService redisService;

    public boolean checkForRateLimiting(String email){
        String key="user:rate:"+email;
        Long count= redisService.incrCount(key);
        if (count==1){
            redisService.setExpiry(key,DURATION);
        }

        if (count<=RATE_PER_DURATION) return true;
        return false;
    }
}
