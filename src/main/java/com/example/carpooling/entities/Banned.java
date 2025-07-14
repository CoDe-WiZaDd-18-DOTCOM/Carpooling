package com.example.carpooling.entities;

import com.example.carpooling.dto.BannedDto;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "banned_list")
public class Banned {

    @Id
    private ObjectId objectId;

    @Indexed(unique = true)
    private String email;

    private String reason;

    private LocalDateTime localDateTime;

    private int duration;

    public Banned() {}

    public Banned(BannedDto bannedDto) {
        this.email = bannedDto.getEmail();
        this.reason = bannedDto.getReason();
        this.localDateTime = bannedDto.getLocalDateTime();
        this.duration = bannedDto.getDuration();
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
