package com.example.demokafkaspringboot.service;

import com.example.demokafkaspringboot.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


@Service
public class ProducerService {
    private static final Logger logger = LoggerFactory.getLogger(ProducerService.class);
    private static final String topic = "demo";

    @Autowired
    private KafkaTemplate<String, String> stringKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, User> userKafkaTemplate;

    public void sendMessage(String message){
        logger.info(String.format("#### -> Producing message string -> %s", message));
        this.stringKafkaTemplate.send(topic, message);
    }

    public void sendMessage(User user) throws JsonProcessingException {
        logger.info(String.format("#### -> Producing message user -> %s", user.toString()));
        ObjectMapper objectMapper = new ObjectMapper();

        Message<String> message = MessageBuilder.withPayload(objectMapper.writeValueAsString(user))
                                                .setHeader(KafkaHeaders.TOPIC, topic)
                                                .build();
        this.userKafkaTemplate.send(message);
//        this.kafkaTemplateUser.send(topic,user);
    }
}
