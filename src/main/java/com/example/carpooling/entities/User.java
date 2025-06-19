package com.example.carpooling.entities;

import com.example.carpooling.enums.Role;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "users")
public class User {

    @Id
    private ObjectId id;

    private String firstName;
    private String lastName;

    @Indexed(unique = true)
    private String email;

    private String phoneNumber;
    private String password;

    private Role role;

    private Preferences preferences;

    private List<ObjectId> currentBookingRequestIds;
    private List<ObjectId> completedBookingRequestIds;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public List<ObjectId> getCurrentBookingRequestIds() {
        return currentBookingRequestIds;
    }

    public void setCurrentBookingRequestIds(List<ObjectId> currentBookingRequestIds) {
        this.currentBookingRequestIds = currentBookingRequestIds;
    }

    public List<ObjectId> getCompletedBookingRequestIds() {
        return completedBookingRequestIds;
    }

    public void setCompletedBookingRequestIds(List<ObjectId> completedBookingRequestIds) {
        this.completedBookingRequestIds = completedBookingRequestIds;
    }
}

