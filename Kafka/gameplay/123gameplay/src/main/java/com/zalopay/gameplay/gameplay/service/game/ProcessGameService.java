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
        switch (userStep){
            case ROCK:
                switch (botStep){
                    case SCISSORS: return WIN;
                    case ROCK: return DRAW;
                    case PAPER: return LOSE;
                }

            case PAPER:
                switch (botStep){
                    case ROCK: return WIN;
                    case PAPER: return DRAW;
                    case SCISSORS: return LOSE;
                }

            case SCISSORS:
                switch (botStep){
                    case PAPER: return WIN;
                    case SCISSORS: return DRAW;
                    case ROCK: return LOSE;
                }
            default: return LOSE;

        }


    }



}
