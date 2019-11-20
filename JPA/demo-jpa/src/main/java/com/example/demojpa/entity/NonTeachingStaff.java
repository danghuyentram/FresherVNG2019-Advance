package com.example.demojpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity

// for single-table strategy
//@DiscriminatorValue(value = "NS")

// for joined strategy
@PrimaryKeyJoinColumn(referencedColumnName = "sid")

// for table per class

@Data
public class NonTeachingStaff extends Staff{
    private String areaexpertise;

    public NonTeachingStaff(String sname, String areaexpertise){
        super(sname);
        this.areaexpertise = areaexpertise;
    }

    public NonTeachingStaff(){
        super();
    }
}
