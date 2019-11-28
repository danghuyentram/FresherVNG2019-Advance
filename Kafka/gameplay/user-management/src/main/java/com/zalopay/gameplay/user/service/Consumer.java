package com.zalopay.gameplay.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zalopay.gameplay.user.model.GameAnnounce;
import com.zalopay.gameplay.user.model.GameResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    @Autowired
    UserGameService userGameService;
    @Autowired
    Producer producer;

    @KafkaListener(topics = "GameResult", groupId = "user")
    public void consumeGameResult(String resultMessage) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        GameResult gameResult = objectMapper.readValue(resultMessage, GameResult.class);
        System.out.println("Game info: ");
        System.out.println(gameResult.toString());
        userGameService.saveUserGamePlay(gameResult.getUserName(), gameResult.getGameType(), gameResult.getResult());
        GameAnnounce gameAnnounce = new GameAnnounce(gameResult.getUserName(),
                                                     gameResult.getUserStep(), gameResult.getBotStep(), gameResult.getResult());
        producer.sendResultToAnnounce(gameAnnounce);
    }
}
