package com.otica.oticaapi.model.people;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.br.CNPJ;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name= "provider")
public class Provider extends Person{
   
    @CNPJ
    private String cnpj;

    @NotBlank
    private String fantasyName;
    
    @NotBlank
    private String corporateName;
}
