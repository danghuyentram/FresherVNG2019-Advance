package com.zalopay.gameplay.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zalopay.gameplay.user.model.GameAnnounce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    @Autowired
    KafkaTemplate<String, GameAnnounce> kafkaTemplate;

    public void sendResultToAnnounce(GameAnnounce gameAnnounce) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Message<String> message = MessageBuilder.withPayload(objectMapper.writeValueAsString(gameAnnounce))
                                                .setHeader(KafkaHeaders.TOPIC, "GameAnnounce").build();
        kafkaTemplate.send(message);
    }

}
