package com.example.carpooling.entities;

import com.example.carpooling.enums.PreferenceOption;
import com.example.carpooling.enums.GenderPreference;

public class Preferences {

    private PreferenceOption music;
    private PreferenceOption smoking;
    private PreferenceOption petFriendly;
    private PreferenceOption ac;
    private GenderPreference genderBased;

    public Preferences() {
    }

    public Preferences(PreferenceOption music, PreferenceOption smoking, PreferenceOption petFriendly, PreferenceOption ac, GenderPreference genderBased) {
        this.music = music;
        this.smoking = smoking;
        this.petFriendly = petFriendly;
        this.ac = ac;
        this.genderBased = genderBased;
    }

    public PreferenceOption getMusic() {
        return music;
    }

    public void setMusic(PreferenceOption music) {
        this.music = music;
    }

    public PreferenceOption getSmoking() {
        return smoking;
    }

    public void setSmoking(PreferenceOption smoking) {
        this.smoking = smoking;
    }

    public PreferenceOption getPetFriendly() {
        return petFriendly;
    }

    public void setPetFriendly(PreferenceOption petFriendly) {
        this.petFriendly = petFriendly;
    }

    public PreferenceOption getAc() {
        return ac;
    }

    public void setAc(PreferenceOption ac) {
        this.ac = ac;
    }

    public GenderPreference getGenderBased() {
        return genderBased;
    }

    public void setGenderBased(GenderPreference genderBased) {
        this.genderBased = genderBased;
    }
}
