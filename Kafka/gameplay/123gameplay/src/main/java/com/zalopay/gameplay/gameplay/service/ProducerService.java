package com.zalopay.gameplay.gameplay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zalopay.gameplay.gameplay.model.GamePlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


@Service
public class ProducerService {
    private static final Logger logger = LoggerFactory.getLogger(ProducerService.class);

    @Value("${kafka.topic}")
    private String topic;


    @Autowired
    private KafkaTemplate<String, GamePlay> kafkaTemplate;



    public void sendMessage(GamePlay gamePlay) throws JsonProcessingException {
        logger.info(String.format("#### -> Producing message gameplay -> %s", gamePlay.toString()));

        ObjectMapper objectMapper = new ObjectMapper();
        Message<String> message = MessageBuilder.withPayload(objectMapper.writeValueAsString(gamePlay))
                                                .setHeader(KafkaHeaders.TOPIC, topic)
                                                .build();
        this.kafkaTemplate.send(message);
    }
}
