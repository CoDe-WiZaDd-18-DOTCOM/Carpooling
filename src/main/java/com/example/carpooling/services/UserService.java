package com.example.carpooling.services;

import com.example.carpooling.dto.UpdateProfileRequest;
import com.example.carpooling.entities.User;
import com.example.carpooling.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean isUserExists(String email){
        return userRepository.existsByEmail(email);
    }

    public User getUser(String email){
        return userRepository.findByEmail(email);
    }

    public void addUser(User user){
        userRepository.save(user);
    }

    public User updateUser(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email);
        if (user == null) return null;

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getPreferences() != null) user.setPreferences(request.getPreferences());

        return userRepository.save(user);
    }


}
