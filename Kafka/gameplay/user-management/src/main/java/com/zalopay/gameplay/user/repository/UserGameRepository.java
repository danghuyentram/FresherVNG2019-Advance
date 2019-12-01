package com.zalopay.gameplay.user.repository;

import com.zalopay.gameplay.user.entity.User;
import com.zalopay.gameplay.user.entity.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    UserGame findUserGameByUserAndGameType(User user, String gameType);

    @Modifying
    @Query(value = "update user_game u set u.total_game = u.total_game + 1 where " +
            "u.user_id = :userId and u.game_type = :gameType", nativeQuery = true)
    void updateTotalGame(@Param("userId") long userId, @Param("gameType") String gameType);
}
