package com.otica.oticaapi.model.people;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name= "provider")
public class Provider extends Person{
   
    private String cnpj;
    private String fantasyName;
    private String corporateName;
}
