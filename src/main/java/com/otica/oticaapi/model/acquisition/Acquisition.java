package com.otica.oticaapi.model.acquisition;

import com.otica.oticaapi.model.people.Provider;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "acquisition")
public class Acquisition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate date;
    private BigDecimal fullValue;

    @OneToMany(mappedBy = "acquisition")
    private List<Acquisition_Product> products;

    @Embedded
    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;
}
