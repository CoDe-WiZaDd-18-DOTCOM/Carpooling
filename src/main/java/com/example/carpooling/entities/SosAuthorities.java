package com.example.carpooling.entities;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@CompoundIndex(name = "unique_area_city", def = "{'area': 1, 'city': 1}", unique = true)
@Document(collection = "sos_authorities")
public class SosAuthorities {

    @Id
    private ObjectId id;

    private String area;
    private String city;

    private String email;

    public SosAuthorities(String area, String city, String email) {
        this.area = area;
        this.city = city;
        this.email = email;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
