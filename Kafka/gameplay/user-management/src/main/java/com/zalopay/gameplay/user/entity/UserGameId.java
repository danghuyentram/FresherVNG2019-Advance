package com.zalopay.gameplay.user.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UserGameId implements Serializable {
    @Column(name = "turn")
    private long turn;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "game_id")
    private long gameId;
    public UserGameId(long userId, long gameId, long turn) {
        this.userId = userId;
        this.gameId = gameId;
        this.turn = turn;
    }
}
