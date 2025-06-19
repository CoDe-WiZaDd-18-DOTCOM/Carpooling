package com.example.carpooling.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    public String getEmail(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
