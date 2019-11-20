package com.example.demospringdatajpa.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstname;
    private String lastname;
    private Date startDate;
    private int age;
    private int active;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @OneToMany( mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Department> departments = new ArrayList<>();


    public User(long id, String firstname, String lastname, Date startDate, int age, int active){
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.startDate = startDate;
        this.age = age;
        this.active = active;
    }

    public void addDepartment(Department department){
        departments.add(department);
        department.setUser(this);
    }

    public void removeDepartment(Department department){
        departments.remove(department);
        department.setUser(null);
    }
    @Override
    public String toString() {
        return "User :  "+this.id +"  "+ this.firstname + " "+
                this.lastname+ " "+this.startDate+" "+this.age+ " "+this.active
                + " "+role.getRoleName();
    }
}
