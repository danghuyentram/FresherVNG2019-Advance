package com.zalopay.gameplay.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameAnnounce {
    private String userName;
    private String name;
    private int userStep;
    private int botStep;
    private int result;
}
