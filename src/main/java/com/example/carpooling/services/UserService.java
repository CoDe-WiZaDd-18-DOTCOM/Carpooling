package com.example.carpooling.services;

import com.example.carpooling.dto.UpdateProfileRequest;
import com.example.carpooling.dto.UserProfileDto;
import com.example.carpooling.entities.User;
import com.example.carpooling.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AnalyticsService analyticsService;

    public boolean isUserExists(String email){
        return userRepository.existsByEmail(email);
    }

    public User getUser(String email){
        return userRepository.findByEmail(email);
    }

    public List<UserProfileDto> getAllUsers(){
        List<User> users = userRepository.findAll();
        List<UserProfileDto> userProfileDtos = new ArrayList<>();
        for (User user:users) userProfileDtos.add(mapToDto(user));
        return userProfileDtos;
    }

    public void addUser(User user){
        userRepository.save(user);
        analyticsService.incUsers();
    }

    public UserProfileDto getUserProfile(String email) {
        String key = "user:"+email+":profile";

        UserProfileDto cachedProfile = redisService.get(key,UserProfileDto.class);
        if (cachedProfile != null) {
            System.out.println("cache hit for profile");
            return cachedProfile;
        }
        System.out.println("cache miss for profile");

        User user = userRepository.findByEmail(email);
        UserProfileDto userProfileDto= mapToDto(user);
        redisService.set(key,userProfileDto,300l);
        return userProfileDto;
    }

    public UserProfileDto updateUserProfile(String email, UserProfileDto dto) {
        User user = userRepository.findByEmail(email);

        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getPhoneNumber() != null) user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getPreferences() != null) user.setPreferences(dto.getPreferences());
        if (dto.getEmergencyEmail() != null) user.setEmergencyEmail(dto.getEmergencyEmail());

        userRepository.save(user);
        return mapToDto(user);
    }

    private UserProfileDto mapToDto(User user) {
        UserProfileDto dto = new UserProfileDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        dto.setPreferences(user.getPreferences());
        dto.setEmergencyEmail(user.getEmergencyEmail());
        dto.setProfileImageBase64(user.getProfileImageBase64());
        dto.setRating(user.getRating());
        dto.setRating_count(user.getRating_count());
        return dto;
    }



}
