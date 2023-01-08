package com.otica.oticaapi.model.people;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.otica.oticaapi.model.address.Address;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Person{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Length(min = 1, max = 80)
    private String name;

    @Length(min = 11, max = 11)
    private String phone;

    @Email
    private String email;

    @NotNull
    private Integer number_house;

    private String complement_house;

    @Embedded
    @ManyToOne
    @JoinColumn(name = "address_cep")
    private Address address;
}
