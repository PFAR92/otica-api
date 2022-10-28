package com.otica.oticaapi.model.people;




import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="client")
public class Client extends Person{
    
    private String cpf;

}
