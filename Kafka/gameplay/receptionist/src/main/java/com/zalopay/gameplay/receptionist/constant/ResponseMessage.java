package com.zalopay.gameplay.receptionist.constant;

public enum ResponseMessage {
    RESPONSE_MESSAGE_GAME123_SUCCESS("Game processing please wait"),
    RESPONSE_MESSAGE_GAME123_FAIL("Invalid user step or game play");

    private String value;
    private ResponseMessage(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
