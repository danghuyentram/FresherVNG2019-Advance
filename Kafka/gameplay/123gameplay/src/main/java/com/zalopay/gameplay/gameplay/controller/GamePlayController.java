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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping(value = "/games/1/verify",consumes = {MediaType.APPLICATION_JSON_VALUE} ,produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity checkExistUserStep(@RequestBody GamePlay gamePlay) {
        if(UserStep.isExistTypeUserPlay(gamePlay.getUserStep())){
            return new ResponseEntity("success", HttpStatus.OK);
        }
        return new ResponseEntity("fail",HttpStatus.BAD_REQUEST);
    }

}
