package com.zalopay.gameplay.receptionist.service;

import com.zalopay.gameplay.receptionist.constant.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


public interface ResponsService {
    ResponseEntity<Object> processRequestGame123Succes();
    ResponseEntity<Object> processRequestGame123Fail();
}
