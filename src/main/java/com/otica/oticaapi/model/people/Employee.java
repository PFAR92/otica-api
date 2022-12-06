package com.otica.oticaapi.model.people;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.br.CPF;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Entity
@Table(name="employee")
public class Employee extends Person{
    
    @CPF
    private String cpf;

    @Min(value = 1112)
    private BigDecimal salary;

    @NotBlank
    private String office;
    private String startDate;
}
