package com.zalopay.gameplay.user.service;

import com.zalopay.gameplay.user.entity.Game;
import com.zalopay.gameplay.user.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    GameRepository gameRepository;

    @Transactional
    public void save(Game game) {
        gameRepository.save(game);
    }

    public Game findGameByName(String name) {
        return gameRepository.findByName(name);
    }

}
