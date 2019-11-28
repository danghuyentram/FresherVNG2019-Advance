package com.zalopay.gameplay.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameResult {
    private String userName;
    private String gameType;
    private int userStep;
    private int botStep;
    private int result;
}
