package com.example.carpooling.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
public class Preferences {
    private boolean music;
    private boolean smoking;
    private boolean petFriendly;
    private GenderPreference genderBased;

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public boolean isPetFriendly() {
        return petFriendly;
    }

    public void setPetFriendly(boolean petFriendly) {
        this.petFriendly = petFriendly;
    }

    public GenderPreference getGenderBased() {
        return genderBased;
    }

    public void setGenderBased(GenderPreference genderBased) {
        this.genderBased = genderBased;
    }
}

