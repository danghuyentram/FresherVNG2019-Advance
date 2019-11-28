package com.zalopay.gameplay.announce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zalopay.gameplay.announce.model.GameAnnounce;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    @KafkaListener(topics = "GameAnnounce", groupId = "123", containerFactory = "kafkaListenerContainerFactory")
    public void consumeGameResult(String resultMessage) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        GameAnnounce gameResult = objectMapper.readValue(resultMessage, GameAnnounce.class);
        System.out.println(gameResult.toString());
    }

}
