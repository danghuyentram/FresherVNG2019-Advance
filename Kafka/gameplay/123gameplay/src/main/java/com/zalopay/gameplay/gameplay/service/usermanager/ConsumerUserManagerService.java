//package com.zalopay.gameplay.gameplay.service.usermanager;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zalopay.gameplay.gameplay.model.GamePlay;
//import com.zalopay.gameplay.gameplay.service.game.ProcessGameService;
//import com.zalopay.gameplay.gameplay.service.game.ProducerGameService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ConsumerUserManagerService {
//    private final Logger logger = LoggerFactory.getLogger(ProducerGameService.class);
//
//    @Autowired
//    ProcessGameService processGameService;
//
//
//    @KafkaListener(topics = "${kafka.topic.userManager}", groupId ="${kafka.consumerGroup}")
//    public void consumeMessage(String message) throws JsonProcessingException {
//
//        // map json to object
//        ObjectMapper objectMapper = new ObjectMapper();
//        GamePlay gamePlay = objectMapper.readValue(message, GamePlay.class);
//
//
////        // process game play
////        gamePlay.setBotStep(processGameService.getBotStep());
////        gamePlay.setResult(processGameService.getResultGame(gamePlay.getUserStep(),gamePlay.getBotStep()));
//
//        logger.info(String.format("#### -> Consumed message of usermanager -> %s",gamePlay.toString()));
//    }
//
//}
