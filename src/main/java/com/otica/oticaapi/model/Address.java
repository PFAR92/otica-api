package com.otica.oticaapi.model;

import com.otica.oticaapi.model.people.Person;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Embeddable
public class Address {

    @Id
    private String cep;
    private String road;
    private Integer number;
    private String complement;
    private String city;
    private String state;
}
