package com.example.demojpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
// for single-table strategy
//@DiscriminatorValue(value = "TS")

// for joined strategy
@PrimaryKeyJoinColumn(referencedColumnName = "sid")

// for table per class
@Data
public class TeachingStaff extends Staff{
    private String qualification;
    private String subjectexpertise;

    public TeachingStaff(){
        super();
    }

    public TeachingStaff( String sname, String qualification, String subjectexpertise){
        super(sname);
        this.qualification = qualification;
        this.subjectexpertise = subjectexpertise;
    }
}
