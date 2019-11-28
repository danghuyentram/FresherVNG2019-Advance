package com.zalopay.gameplay.gameplay.service.usermanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zalopay.gameplay.gameplay.model.GamePlay;
//import com.zalopay.gameplay.gameplay.service.game.ProducerGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ProducerUserManagerService {
//    private static final Logger logger = LoggerFactory.getLogger(ProducerGameService.class);

    @Value("${kafka.topic.userManager}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, GamePlay> kafkaTemplate;


    public void sendMessage(GamePlay gamePlay) throws JsonProcessingException {
//        logger.info(String.format("#### -> Producing message user manager -> %s", gamePlay.toString()));

        ObjectMapper objectMapper = new ObjectMapper();
        Message<String> message = MessageBuilder.withPayload(objectMapper.writeValueAsString(gamePlay))
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
        this.kafkaTemplate.send(message);
    }
}
