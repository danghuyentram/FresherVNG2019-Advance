package com.zalopay.gameplay.user.repository;

import com.zalopay.gameplay.user.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    Game findByName(String name);
}
