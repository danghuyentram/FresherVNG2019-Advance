package com.zalopay.gameplay.user.repository;

import com.zalopay.gameplay.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
