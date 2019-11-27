package com.zalopay.gameplay.gameplay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zalopay.gameplay.gameplay.model.GamePlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    private final Logger logger = LoggerFactory.getLogger(ProducerService.class);

    @Autowired
    ProcessGameService processGameService;

    @Value("${kafka.consumerGroup}")
    String consumerGroup;

    ;

    @KafkaListener(topics =( @Value("${kafka.topic}") String topic), groupId = "consumerGroup2")
    public void consumeMessage(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        GamePlay gamePlay = objectMapper.readValue(message, GamePlay.class);

        gamePlay.setBotStep(processGameService.getBotStep());
        gamePlay.setResult(processGameService.getResultGame(gamePlay.getUserStep(),gamePlay.getBotStep()));

        logger.info(String.format("#### -> Consumed message of string -> %s",GamePlay.class));
    }



}
