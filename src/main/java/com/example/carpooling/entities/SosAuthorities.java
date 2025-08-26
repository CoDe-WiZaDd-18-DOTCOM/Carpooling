package com.example.carpooling.entities;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "sos_authorities")
public class SosAuthorities {

    @Id
    private String id;

    @Indexed(unique = true)
    private String label;

    private String email;

    public SosAuthorities(String label, String email) {
        this.label = label;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getEmail() {
        if(email==null) return "";
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
