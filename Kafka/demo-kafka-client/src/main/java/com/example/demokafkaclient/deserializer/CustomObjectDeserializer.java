package com.example.demokafkaclient.deserializer;

import com.example.demokafkaclient.pojo.CustomObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class CustomObjectDeserializer implements Deserializer<CustomObject> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public CustomObject deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        CustomObject object = null;
        try {
            object = mapper.readValue(bytes, CustomObject.class);
        } catch (Exception exception) {
            System.out.println("Error in deserializing bytes "+ exception);
        }
        return object;
    }

    @Override
    public CustomObject deserialize(String topic, Headers headers, byte[] data) {
        return null;
    }

    @Override
    public void close() {

    }
}
