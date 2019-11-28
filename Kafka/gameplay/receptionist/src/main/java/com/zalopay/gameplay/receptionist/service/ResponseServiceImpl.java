package com.zalopay.gameplay.receptionist.service;

import com.zalopay.gameplay.receptionist.constant.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseServiceImpl implements ResponsService {

    @Override
    public ResponseEntity<Object> processRequestGame123Succes() {
        return new ResponseEntity<>(ResponseMessage.RESPONSE_MESSAGE_GAME123_SUCCESS.getValue(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> processRequestGame123Fail() {
        return new ResponseEntity<>(ResponseMessage.RESPONSE_MESSAGE_GAME123_FAIL.getValue(), HttpStatus.BAD_REQUEST);
    }

}
