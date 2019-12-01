package com.zalopay.gameplay.gameplay.model;

import com.zalopay.gameplay.gameplay.constant.UserStep;
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
    private UserStep userStep;
    private int botStep;
    private int result;


    public GamePlay(String username){
        this.userName = username;
//        this.userStep = userStep;
        this.botStep = 0;
        this.result = 0;
    }

}
