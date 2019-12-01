package com.zalopay.gameplay.user.service;

import com.zalopay.gameplay.user.entity.User;

public interface UserService {
    void save(User user);
    User findUserByUsername(String username);
}
