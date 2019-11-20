package com.example.demorediscounter.repository;

import com.example.demorediscounter.entity.Counter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository<Counter, Long> {
}
