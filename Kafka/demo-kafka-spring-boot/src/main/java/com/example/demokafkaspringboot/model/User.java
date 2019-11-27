package com.example.demokafkaspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private int age;

    @Override
    public String toString(){
        return "User: name: " +this.name +" - age: "+ this.age ;
    }
}
