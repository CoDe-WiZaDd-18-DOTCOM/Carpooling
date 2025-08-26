package com.example.carpooling.config;

import com.fasterxml.jackson.core.JsonParser;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import org.bson.types.ObjectId;
import java.io.IOException;

@Configuration
public class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {
    public ObjectIdDeserializer() {} // ✅ no-arg constructor

    @Override
    public ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new ObjectId(p.getValueAsString());
    }
}
