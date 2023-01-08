package com.otica.oticaapi.model.sale;


import com.otica.oticaapi.model.people.Client;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private BigDecimal fullValue;
    private Integer discount;
    private BigDecimal totalValue;

    @OneToMany(mappedBy = "sale")
    private List<Sale_Product> products;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

}
