package com.zalopay.gameplay.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "random_name")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RandomUsername {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
}
