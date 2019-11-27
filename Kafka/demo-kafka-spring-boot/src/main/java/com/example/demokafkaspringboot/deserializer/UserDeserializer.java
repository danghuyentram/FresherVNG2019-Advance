package com.example.demokafkaspringboot.deserializer;

import com.example.demokafkaspringboot.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class UserDeserializer implements Deserializer<User> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public User deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        User object = null;
        try {
            object = mapper.readValue(bytes, User.class);
        } catch (Exception exception) {
            System.out.println("Error in deserializing bytes "+ exception);
        }
        return object;
    }

    @Override
    public User deserialize(String topic, Headers headers, byte[] data) {
        return null;
    }

    @Override
    public void close() {

    }
}
