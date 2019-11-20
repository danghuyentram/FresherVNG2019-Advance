package com.example.demospringdatajpa.entity;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String roleName;

    @OneToMany( mappedBy = "role",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    public Role(long id, String roleName){
        this.id = id;
        this.roleName = roleName;
    }

    public void addUser(User user){
        users.add(user);
        user.setRole(this);
    }

    public void removeUser(User user){
        users.remove(user);
        user.setRole(null);
    }

    @Override
    public String toString() {
        return "Role: "+this.id +" "+this.roleName;
    }
}
