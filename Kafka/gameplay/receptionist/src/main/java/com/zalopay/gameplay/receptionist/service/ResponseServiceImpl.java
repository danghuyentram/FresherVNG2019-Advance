package com.zalopay.gameplay.receptionist.service;

import com.zalopay.gameplay.receptionist.constant.ResponseMessage;
import com.zalopay.gameplay.receptionist.controller.HandleRequestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseServiceImpl implements ResponsService {

    private final Logger logger = LoggerFactory.getLogger(ResponseServiceImpl.class);


    @Override
    public ResponseEntity<Object> processRequestGame123Succes() {
        logger.info("request play game success, send request to service game 123");
        return new ResponseEntity<>(ResponseMessage.RESPONSE_MESSAGE_GAME123_SUCCESS.getValue(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> processRequestGame123Fail() {
        logger.info("request play game fail, invalid userStep or game type");
        return new ResponseEntity<>(ResponseMessage.RESPONSE_MESSAGE_GAME123_FAIL.getValue(), HttpStatus.BAD_REQUEST);
    }

}
