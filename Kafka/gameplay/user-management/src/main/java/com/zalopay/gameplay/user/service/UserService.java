package com.zalopay.gameplay.user.service;

import com.zalopay.gameplay.user.entity.User;

public interface UserService {
    public void save(User user);
    public User findUserByUsername(String username);
}
