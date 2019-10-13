package com.example.demotest.repository;


import com.example.demotest.model.Todo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository {
    List<Todo> findAll();
    Todo findById(int id);
}
