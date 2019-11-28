//package com.zalopay.gameplay.gameplay.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.zalopay.gameplay.gameplay.model.GamePlay;
//import com.zalopay.gameplay.gameplay.service.game.ProcessGameService;
//import com.zalopay.gameplay.gameplay.service.game.ProducerGameService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//
//@Controller
//public class GamePlayController {
//    @Autowired
//    private ProducerGameService producerGameService;
//
//    @Autowired
//    private ProcessGameService processGameService;
//
//
//    @RequestMapping(value = "/game123")
//    public ResponseEntity sendMessageUserToKafkaTopic(@RequestParam("username") String name, @RequestParam("userstep") int userStep) throws JsonProcessingException {
//        GamePlay gamePlay = new GamePlay(name,userStep);
//
//
//        this.producerGameService.sendMessage(gamePlay);
//        return ResponseEntity.accepted().body("Send message: "+gamePlay.toString()+" success");
//    }
//
//}
