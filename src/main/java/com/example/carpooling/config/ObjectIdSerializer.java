package com.example.carpooling.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@Configuration
public class ObjectIdSerializer extends JsonSerializer<ObjectId> {
    public ObjectIdSerializer() {}

    @Override
    public void serialize(ObjectId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.toHexString());
    }
}
