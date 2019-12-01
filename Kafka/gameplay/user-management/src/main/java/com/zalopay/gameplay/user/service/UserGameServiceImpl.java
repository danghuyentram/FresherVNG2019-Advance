package com.zalopay.gameplay.user.service;

import com.zalopay.gameplay.user.entity.User;
import com.zalopay.gameplay.user.entity.UserGame;
import com.zalopay.gameplay.user.repository.UserGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class UserGameServiceImpl implements UserGameService {

    @Autowired
    UserService userService;
    @Autowired
    UserGameRepository userGameRepository;
    @Autowired
    UtilService utilService;

    @Override
    @Transactional
    public void saveUserGamePlay(String username, String gameType) {
        User user = userService.findUserByUsername(username);
        // first play, save user info and then process game play
        if(user == null) {
            user = new User();
            user.setUsername(username);
            Random r = new Random();
            user.setName(utilService.findById(r.nextInt( (int) utilService.countAllRandomName()) + 1).getName());
            userService.save(user);
            user = userService.findUserByUsername(username);
        }

        UserGame userGame = userGameRepository.findUserGameByUserAndGameType(user, gameType);

        if(userGame == null) {
            userGame = new UserGame();
            userGame.setUser(user);
            userGame.setGameType(gameType);
            userGameRepository.save(userGame);
        }
        userGameRepository.updateTotalGame(user.getId(), gameType);
    }
}
