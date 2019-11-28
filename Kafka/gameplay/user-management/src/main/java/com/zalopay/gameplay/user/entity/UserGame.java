package com.zalopay.gameplay.user.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_game")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserGame {
    @EmbeddedId
    private UserGameId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("game_id")
    private Game game;

    private int result;

}
