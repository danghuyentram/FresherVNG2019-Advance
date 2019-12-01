package com.zalopay.gameplay.receptionist.service;

import com.zalopay.gameplay.receptionist.constant.UserStep;
import com.zalopay.gameplay.receptionist.model.RequestGame123;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ValidateRequestGame123Service {
    @Autowired
    RestTemplate restTemplate;

    private final String urlCheckTypeUserStep = "http://localhost:8081/games/1";

    public boolean isExistTypeUserPlay(UserStep userStep){
        //Config http request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlCheckTypeUserStep)
                .queryParam("userStep", userStep);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseFrom123Game = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

        //if reponse ok, have userStep in game 123
        if(responseFrom123Game.getStatusCode() == HttpStatus.OK)
            return true;
        //don't have user step
        return false;
    }
}
