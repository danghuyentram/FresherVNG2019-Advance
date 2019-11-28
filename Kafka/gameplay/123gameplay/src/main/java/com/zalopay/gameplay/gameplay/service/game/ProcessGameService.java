package com.zalopay.gameplay.gameplay.service.game;


import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ProcessGameService {
    public static final int MAX = 2;
    public static final int MIN = 0;
    public static final int ROCK = 0;
    public static final int PAPER = 1;
    public static final int SCISSORS = 2;
    public static final int WIN = 0;
    public static final int LOSE = 1;
    public static final int DRAW = 2;
    private final Random botStep = new Random();


    public int getBotStep(){
        return botStep.nextInt(3);
    }


    public int getResultGame(int userStep, int botStep){
        if(userStep == botStep)
            return DRAW;
        else if(userStep == SCISSORS)
            // userStep = SCISSORS
                    if(botStep == ROCK)
                        // botStep = ROCK
                        return LOSE;
                    else
                        // botStep = PAPER
                        return WIN;
        else if(userStep == ROCK)
            // userStep = ROCK
                    if(botStep == PAPER)
                        // botStep = PAPER
                        return LOSE;
                    else
                        // botStep = SCISSORS
                        return WIN;
        else
            // userStep = PAPER
            if(botStep == SCISSORS)
                // botStep = SCISSORS
                return LOSE;
            else
                // botStep = ROCK
                return WIN;
    }



}
