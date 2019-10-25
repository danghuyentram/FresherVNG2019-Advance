package com.example.demopropagation.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private String empId;
    private String empName;

    @Override
    public String toString(){
        return "Employee [empId=" + empId + ", empName=" + empName + "]";
    }
}
