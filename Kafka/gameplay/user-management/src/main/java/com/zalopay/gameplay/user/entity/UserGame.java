package com.zalopay.gameplay.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_game")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String gameType;
    private long totalGame;
}
