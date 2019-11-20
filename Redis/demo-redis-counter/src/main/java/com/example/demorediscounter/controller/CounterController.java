package com.example.demorediscounter.controller;

import com.example.demorediscounter.repository.CounterRepository;
import com.example.demorediscounter.service.CounterService;
import org.apache.coyote.Response;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;

@Controller
public class CounterController {
    @Autowired
    CounterRepository counterRepository;

    @Autowired
    CounterService counterService;

    @RequestMapping("/ping1")
    public @ResponseBody ResponseEntity pingAtomic(){

        return ResponseEntity.accepted().body(counterService.setCounterAtomic());
    }

    @RequestMapping("/ping2")
    public @ResponseBody ResponseEntity ping(){

        return ResponseEntity.accepted().body(counterService.setCounter());
    }


    @RequestMapping("/test")
    public @ResponseBody ResponseEntity testCluster(){

        return ResponseEntity.accepted().body(counterService.test());
    }





}


