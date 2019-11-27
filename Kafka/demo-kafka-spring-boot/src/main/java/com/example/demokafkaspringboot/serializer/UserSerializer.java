package com.example.demokafkaspringboot.serializer;

import com.example.demokafkaspringboot.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UserSerializer implements Serializer<User> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String s, User user) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(user).getBytes();
        } catch (Exception exception) {
            System.out.println("Error in serializing object"+ user);
        }
        return retVal;
    }

    @Override
    public byte[] serialize(String topic, Headers headers, User data) {
        return new byte[0];
    }

    @Override
    public void close() {

    }
}
