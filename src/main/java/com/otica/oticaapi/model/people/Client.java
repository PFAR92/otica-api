package com.otica.oticaapi.model.people;




import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.br.CPF;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Entity
@Table(name="client")
public class Client extends Person{
    
    @CPF
    private String cpf;

}
