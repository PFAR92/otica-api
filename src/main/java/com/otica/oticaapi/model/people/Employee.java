package com.otica.oticaapi.model.people;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="employee")
public class Employee extends Person{
    
    private String cpf;
    private Double salary;
    private String office;
    private String startDate;
}
