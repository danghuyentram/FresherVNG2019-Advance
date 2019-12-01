package com.zalopay.gameplay.receptionist.constant;

import java.util.HashMap;
import java.util.Map;

public enum UserStep {
    ROCK (0),
    PAPER (1),
    SCISSORS (2);

    private final Integer value;

    private static Map listTypeUserPlay= new HashMap<>();

    private UserStep(final Integer value) {
        this.value = value;
    }

    static {
        for (UserStep userStep : UserStep.values()) {
            listTypeUserPlay.put(userStep.value, userStep);
        }
    }
    public static boolean isExistTypeUserPlay(Integer typeUserPlay){
        return listTypeUserPlay.containsKey(typeUserPlay);
    }
    public static UserStep valueOf(Integer typeUserPlay) {
        return (UserStep) listTypeUserPlay.get(typeUserPlay);
    }
    public Integer getValue() { return value; }
}
