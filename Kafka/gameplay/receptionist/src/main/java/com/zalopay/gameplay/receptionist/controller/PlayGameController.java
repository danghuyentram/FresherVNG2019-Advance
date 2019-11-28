package com.zalopay.gameplay.receptionist.controller;

import com.zalopay.gameplay.receptionist.model.RequestGame123;
import com.zalopay.gameplay.receptionist.service.HandleRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayGameController {

    @Autowired
    HandleRequestService handleRequestService;

    @PostMapping(path = "/games/123")
    public ResponseEntity<Object> requestPlayGame(@RequestBody RequestGame123 requestGame123){
        return handleRequestService.ValidateAndSendRequestGame123(requestGame123);
    }
}
