package com.zalopay.gameplay.user.repository;

import com.zalopay.gameplay.user.entity.RandomUsername;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RandomUsernameRepository extends JpaRepository<RandomUsername, Long> {
    RandomUsername findById(long id);
}
