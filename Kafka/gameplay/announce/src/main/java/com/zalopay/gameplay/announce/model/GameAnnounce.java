package com.zalopay.gameplay.announce.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GameAnnounce {
    private String userName;
    private int userStep;
    private int botStep;
    private int result;
}
