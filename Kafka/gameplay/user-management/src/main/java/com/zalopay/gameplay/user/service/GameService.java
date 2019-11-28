package com.zalopay.gameplay.user.service;

import com.zalopay.gameplay.user.entity.Game;

public interface GameService {
    public void save(Game game);
    public Game findGameByName(String name);
}
