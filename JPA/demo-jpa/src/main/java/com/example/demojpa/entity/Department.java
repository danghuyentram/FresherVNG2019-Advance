package com.example.demojpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name ="seqGen", sequenceName = "seqDB", initialValue = 3, allocationSize = 50)

    private int id;
    private String name;

    @OneToMany(targetEntity = Employee.class)
    private List employeeList;

}
