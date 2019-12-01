package com.zalopay.gameplay.receptionist.controller;

import com.zalopay.gameplay.receptionist.model.RequestGame123;
import com.zalopay.gameplay.receptionist.service.HandleRequestService;
import com.zalopay.gameplay.receptionist.service.ResponsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HandleRequestController {

    @Autowired
    HandleRequestService handleRequestService;

    @Autowired
    ResponsService responsService;

    private final Logger logger = LoggerFactory.getLogger(HandleRequestController.class);

        @PostMapping(path = "/games/1")
    public ResponseEntity<Object> requestPlayGame(@RequestBody RequestGame123 requestGame123){
            logger.info("request play game 123");
            return handleRequestService.ValidateAndSendRequestGame123(requestGame123);
    }
}
