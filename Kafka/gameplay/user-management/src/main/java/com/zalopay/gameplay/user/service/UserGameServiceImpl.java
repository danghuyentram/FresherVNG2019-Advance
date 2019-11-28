package com.zalopay.gameplay.user.service;

import com.zalopay.gameplay.user.entity.Game;
import com.zalopay.gameplay.user.entity.User;
import com.zalopay.gameplay.user.entity.UserGame;
import com.zalopay.gameplay.user.entity.UserGameId;
import com.zalopay.gameplay.user.repository.UserGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class UserGameServiceImpl implements UserGameService {

    @Autowired
    UserService userService;
    @Autowired
    GameService gameService;
    @Autowired
    UserGameRepository userGameRepository;
    @Autowired
    UtilService utilService;

    @Override
    @Transactional
    public void saveUserGamePlay(String username, String gameType, int result) {
        User user = userService.findUserByUsername(username);
        // first play, save user info and then process game play
        if(user == null) {
            user = new User();
            user.setUsername(username);
            Random r = new Random();
            user.setName(utilService.findById(r.nextInt(4) + 1).getName());
            userService.save(user);
        }

        Game game = gameService.findGameByName(gameType);
        // determine turn ?
        List<UserGame> totalGamePlay = findByUserAndGame(user, game);
        int turn = totalGamePlay.size() + 1;
        // set info
        UserGameId id = new UserGameId(user.getId(), game.getId(), turn);

        UserGame userGame = new UserGame(id, user, game, result);

        List<UserGame> games = user.getGames();
        games.add(userGame);
    }

    @Override
    public List<UserGame> findByUserAndGame(User user, Game game) {
        return userGameRepository.findByUserAndGame(user, game);
    }
}
