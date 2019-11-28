package com.zalopay.gameplay.user.repository;

import com.zalopay.gameplay.user.entity.Game;
import com.zalopay.gameplay.user.entity.User;
import com.zalopay.gameplay.user.entity.UserGame;
import com.zalopay.gameplay.user.entity.UserGameId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGameRepository extends JpaRepository<UserGame, UserGameId> {
    List<UserGame> findByUserAndGame(User user, Game game);
}
