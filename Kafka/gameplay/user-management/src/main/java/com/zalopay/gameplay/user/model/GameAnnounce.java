package com.zalopay.gameplay.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameAnnounce {
    private String userName;
    private String name;
    private int userStep;
    private int botStep;
    private int result;

    public GameAnnounce(String userName, int userStep, int botStep, int result) {
        this.userName = userName;
        this.userStep = userStep;
        this.botStep = botStep;
        this.result = result;
    }
}
