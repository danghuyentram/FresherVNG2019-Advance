package com.zalopay.gameplay.gameplay.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GamePlay {
    private String userName;
    private String gameType;
    private int userStep;
    private int botStep;
    private int result;

    public GamePlay(String username, int userStep){
        this.userName = username;
        this.userStep = userStep;
        this.botStep = 0;
        this.result = 0;
    }

}
