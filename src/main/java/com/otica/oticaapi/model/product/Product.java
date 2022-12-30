package com.otica.oticaapi.model.product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.otica.oticaapi.model.acquisition.Acquisition_Product;
import com.otica.oticaapi.model.sale.Sale_Product;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 1, max = 20)
    private String name;
    @NotNull
    private BigDecimal value;
    @Length(min = 1, max = 200)
    private String description;
    @NotNull
    private BigDecimal purchase_price;
    @NotBlank
    private String model;

    private Long quantity;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Acquisition_Product> acquisitions;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Sale_Product> sales;

}
