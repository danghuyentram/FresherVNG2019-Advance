package com.example.demokafkaspringboot.controller;

import com.example.demokafkaspringboot.model.User;
import com.example.demokafkaspringboot.service.ProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/kafka")
public class KafkaController {
    @Autowired
    private  ProducerService producerService;

    @RequestMapping(value = "/publish")
    public ResponseEntity sendMessageStringToKafkaTopic(@RequestParam("message") String message){
        this.producerService.sendMessage(message);
        return ResponseEntity.accepted().body("Send message: "+message+" success");
    }

    @RequestMapping(value = "/publish/user")
    public ResponseEntity sendMessageUserToKafkaTopic(@RequestParam("name") String name, @RequestParam("age") int age) throws JsonProcessingException {
        User user = new User(name,age);
        this.producerService.sendMessage(user);
        return ResponseEntity.accepted().body("Send message: "+user.toString()+" success");
    }

}
