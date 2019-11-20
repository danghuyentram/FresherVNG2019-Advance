package com.example.demojpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table

// @Inheritance( strategy = InheritanceType.SINGLE_TABLE)
@Inheritance(strategy = InheritanceType.JOINED)
// @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

@DiscriminatorColumn(name = "type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int sid;
    private String sname;

    public Staff(String sname){
        this.sname = sname;
    }

}
