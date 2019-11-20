package com.example.demospringdatajpa.repository;

import com.example.demospringdatajpa.entity.Role;
import com.example.demospringdatajpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends JpaRepository<Role, Long>, QuerydslPredicateExecutor<Role> {
    Role findByUsersId(long id);
}
