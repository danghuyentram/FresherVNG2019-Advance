package com.example.demojdbcconnectdb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Employee {
    private int id;
    private String name;
    private String position;
    private double salary;

    // standard constructor, getters, setters
}