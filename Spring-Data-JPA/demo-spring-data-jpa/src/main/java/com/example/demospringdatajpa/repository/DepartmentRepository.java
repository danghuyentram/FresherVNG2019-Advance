package com.example.demospringdatajpa.repository;

import com.example.demospringdatajpa.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long>, QuerydslPredicateExecutor<Department> {
    Department findById(long id);
    long countByUserId(long id);
}
