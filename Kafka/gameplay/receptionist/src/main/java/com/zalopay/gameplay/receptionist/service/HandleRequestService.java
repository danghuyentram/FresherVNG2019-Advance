package com.zalopay.gameplay.receptionist.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zalopay.gameplay.receptionist.constant.GameType;
import com.zalopay.gameplay.receptionist.constant.Gesture;
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
    ResponsService responsService;

    @Transactional
    public ResponseEntity<Object> ValidateAndSendRequestGame123(RequestGame123 requestGame123){
        if(requestGame123 == null){
            return responsService.processRequestGame123Fail();
        }
        GameType gameType = GameType.valueof(requestGame123.getGameType());
        boolean isGameExist = GameType.isExistGameType(gameType);
        if(!isGameExist){
            return responsService.processRequestGame123Fail();
        }
        boolean isTypeUserPlayExist = Gesture.isExistTypeUserPlay(requestGame123.getUserStep());
        if(!isTypeUserPlayExist){
            return responsService.processRequestGame123Fail();
        }
        String topic = gameType.getTopic();
        try{
            producerRequestGame123.sendRequestGame123(requestGame123,topic);
        }catch (JsonProcessingException json){
            return responsService.processRequestGame123Fail();
        }
        return responsService.processRequestGame123Succes();
    }

}
