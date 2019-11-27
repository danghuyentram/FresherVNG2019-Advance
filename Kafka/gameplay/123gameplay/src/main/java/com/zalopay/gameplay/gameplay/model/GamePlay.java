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
    private int userStep;
    private int botStep;
    private int result;

}
