package com.zalopay.gameplay.receptionist.constant;

public enum ResponseMessage {
    RESPONSE_MESSAGE_GAME123_SUCCESS("Game 123: processing request please wait"),
    RESPONSE_MESSAGE_GAME123_FAIL("Game 123: Invalid user step or game play");

    private String value;
    private ResponseMessage(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
