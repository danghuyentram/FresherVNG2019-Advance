package com.example.demopropagation.dao;

import com.example.demopropagation.model.Employee;

public interface EmployeeDao {
    void insertEmployee(Employee employee);

    void deleteEmployeeById(String empid);
}
