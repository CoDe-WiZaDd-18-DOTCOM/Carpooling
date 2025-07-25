package com.example.carpooling.services;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private static final Logger log = LoggerFactory.getLogger(RedisService.class);
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T get(String key, Class<T> entityClass) {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            return objectMapper.readValue(o.toString(), entityClass);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public <T> List<T> getList(String key, Class<T> elementType) {
        try {
            Object json = redisTemplate.opsForValue().get(key);
            if (json==null) return null;

            JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, elementType);
            return objectMapper.readValue(json.toString(), type);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public void set(String key, Object o, Long ttl) {
        try {
            String jsonValue = objectMapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

}
