package com.example.carpooling.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    public String getEmail(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
