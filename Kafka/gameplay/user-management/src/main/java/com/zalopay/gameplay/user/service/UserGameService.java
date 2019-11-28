package com.zalopay.gameplay.user.service;

import com.zalopay.gameplay.user.entity.Game;
import com.zalopay.gameplay.user.entity.User;
import com.zalopay.gameplay.user.entity.UserGame;

import java.util.List;

public interface UserGameService {
    public void saveUserGamePlay(String username, String gameType, int result);
    List<UserGame> findByUserAndGame(User user, Game game);
}
