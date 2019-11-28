package com.zalopay.gameplay.receptionist.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.zalopay.gameplay.receptionist.model.RequestGame123;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class ProducerRequestGame123 {

    @Autowired
    KafkaTemplate<String, RequestGame123> kafkaTemplate;

    private final JsonMapper jsonMapper = new JsonMapper();

    public void sendRequestGame123(RequestGame123 requestGame123, String topic) throws JsonProcessingException {
        Message<String> message = MessageBuilder
                .withPayload(jsonMapper.writeValueAsString(requestGame123))
                .setHeader(KafkaHeaders.TOPIC,topic)
                .build();
        kafkaTemplate.send(message);
    }
}
