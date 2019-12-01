package com.example.demokafkaspringboot.service;

import com.example.demokafkaspringboot.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    private final Logger logger = LoggerFactory.getLogger(ProducerService.class);



    @KafkaListener(topics = "demo", groupId = "consumerGroup2")
    public void consume2(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(message, User.class);
        logger.info(String.format("#### -> Consumed message of string %s-> %s","consumer2",user));
    }

//    @KafkaListener(topics = "demo", groupId = "consumerGroup2")
//    public void consume3(@Payload User user, @Headers MessageHeaders headers){
//        logger.info(String.format("#### -> Consumed message of user %s-> %s","consumer3",user));
//
//    }

//    @KafkaListener(topics = "demo", groupId = "consumerGroup2")
//    public void consume3(@Payload String user, @Headers MessageHeaders headers){
//
//        logger.info(String.format("#### -> Consumed message of %s-> %s","consumer3",user));
//
//    }

}
