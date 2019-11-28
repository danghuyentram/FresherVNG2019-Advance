package com.zalopay.gameplay.gameplay.service.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zalopay.gameplay.gameplay.model.GamePlay;
import com.zalopay.gameplay.gameplay.service.usermanager.ProducerUserManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ConsumerGameService {
//    private final Logger logger = LoggerFactory.getLogger(ProducerGameService.class);

    @Autowired
    ProcessGameService processGameService;

    @Autowired
    ProducerUserManagerService producerUserManagerService;

    private final ObjectMapper objectMapper = new ObjectMapper();



    @KafkaListener(topics = "${kafka.topic.game}", groupId ="${kafka.consumerGroup}")
    public void consumeMessage(String message) throws IOException {

        // map json to object
        GamePlay gamePlay = objectMapper.readValue(message, GamePlay.class);


        // process game play
        gamePlay.setBotStep(processGameService.getBotStep());
        gamePlay.setResult(processGameService.getResultGame(gamePlay.getUserStep(),gamePlay.getBotStep()));

//        logger.info(String.format("#### -> Consumed message of gameplay -> %s",gamePlay.toString()));


        // produce to userManager topic
        producerUserManagerService.sendMessage(gamePlay);

    }



}
