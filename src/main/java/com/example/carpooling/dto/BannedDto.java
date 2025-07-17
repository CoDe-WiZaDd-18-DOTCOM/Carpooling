package com.example.carpooling.dto;

import java.time.LocalDateTime;

public class BannedDto {
    private String email;
    private String reason;
    private LocalDateTime localDateTime;
    private int duration;

    public BannedDto(String email, String reason, LocalDateTime localDateTime, int duration) {
        this.email = email;
        this.reason = reason;
        this.localDateTime = localDateTime;
        this.duration = duration;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
