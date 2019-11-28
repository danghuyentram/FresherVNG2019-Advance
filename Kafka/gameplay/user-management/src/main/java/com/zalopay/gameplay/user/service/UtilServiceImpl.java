package com.zalopay.gameplay.user.service;

import com.zalopay.gameplay.user.entity.RandomUsername;
import com.zalopay.gameplay.user.repository.RandomUsernameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtilServiceImpl implements UtilService {

    @Autowired
    RandomUsernameRepository randomUsernameRepository;

    @Override
    public RandomUsername findById(long id) {
        return randomUsernameRepository.findById(id);
    }
}
