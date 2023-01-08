package com.otica.oticaapi.model.address;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Embeddable
public class Address {

    @Id
    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
}
