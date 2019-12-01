package com.zalopay.gameplay.gameplay.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zalopay.gameplay.gameplay.constant.UserStep;
import com.zalopay.gameplay.gameplay.model.GamePlay;
import com.zalopay.gameplay.gameplay.service.game.ConsumerGameService;
import com.zalopay.gameplay.gameplay.service.game.ProcessGameService;
//import com.zalopay.gameplay.gameplay.service.game.ProducerGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GamePlayController {
//    @Autowired
//    private ProducerGameService producerGameService;

    @Autowired
    private ProcessGameService processGameService;
    private final Logger logger = LoggerFactory.getLogger(ConsumerGameService.class);


//    @RequestMapping(value = "/game123")
//    public ResponseEntity sendMessageUserToKafkaTopic(@RequestParam("username") String name, @RequestParam("userstep") int userStep) throws JsonProcessingException {
//        GamePlay gamePlay = new GamePlay(name,userStep);
//
//
//        this.producerGameService.sendMessage(gamePlay);
//        return ResponseEntity.accepted().body("Send message: "+gamePlay.toString()+" success");
//    }

    @GetMapping("/games/1")
    public ResponseEntity validateGameData(@RequestParam(name = "userStep") Integer userStep){
        logger.info("in controller check userStep exist");
//        if(UserStep.isExistTypeUserPlay(userStep))
//            return new ResponseEntity<String>("ok", HttpStatus.OK);
        return new ResponseEntity("invalid userStep",HttpStatus.BAD_REQUEST);
    }

}
