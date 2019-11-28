package com.zalopay.gameplay.user.service;

import com.zalopay.gameplay.user.entity.User;
import com.zalopay.gameplay.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }



}
