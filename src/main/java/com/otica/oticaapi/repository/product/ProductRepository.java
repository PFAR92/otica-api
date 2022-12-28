package com.otica.oticaapi.repository.product;

import com.otica.oticaapi.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String name);
    Boolean existsByName(String name);
    Boolean existsByModel(String model);

    boolean existsByNameAndModel(String name, String model);

    Product findByNameAndModel(String name, String model);

}