package com.zalopay.gameplay.receptionist.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zalopay.gameplay.receptionist.constant.GameType;
import com.zalopay.gameplay.receptionist.constant.UserStep;
import com.zalopay.gameplay.receptionist.model.RequestGame123;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HandleRequestService {

    @Autowired
    ProducerRequestGame123 producerRequestGame123;

    @Autowired
    ValidateRequestGame123Service validateRequestGame123Service;

    @Autowired
    ResponsService responsService;

    public ResponseEntity<Object> ValidateAndSendRequestGame123(RequestGame123 requestGame123){
        //check request play game wrong type or wrong structure
        if(requestGame123 == null){
            return responsService.processRequestGame123Fail();
        }

        boolean isTypeUserPlayExist = validateRequestGame123Service.
                isExistTypeUserPlay(requestGame123.getUserStep());
        if(!isTypeUserPlayExist){
            return responsService.processRequestGame123Fail();
        }
        // send request to service 123 game play
        try{
            producerRequestGame123.sendRequestGame123(requestGame123,GameType.GAME123.getTopic());
        }catch (JsonProcessingException json){
            return responsService.processRequestGame123Fail();
        }
        return responsService.processRequestGame123Succes();
    }

}
