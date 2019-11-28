package com.zalopay.gameplay.receptionist.constant;

import java.util.HashMap;
import java.util.Map;

public enum  GameType {
    GAME123("123GamePlay");

    private final String topicOfGame;

    private static Map listGameType = new HashMap<>();

    GameType(final String topicOfGame){
        this.topicOfGame = topicOfGame;
    }

    static {
        for (GameType gameType : GameType.values()) {
            listGameType.put(gameType.getTopic(), gameType);
        }
    }

    public String getTopic() {
        return topicOfGame;
    }

    public static GameType valueof(String value){
        GameType gameType = (GameType) listGameType.get(value);
        if (gameType == null){
            throw new IllegalArgumentException("Not Enum constant was found for value : " + value);
        }
        return gameType;
    }
    public static boolean isExistGameType(GameType gameType){
        return listGameType.containsValue(gameType);
    }
}
