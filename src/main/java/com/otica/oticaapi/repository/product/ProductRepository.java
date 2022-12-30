package com.otica.oticaapi.repository.product;

import com.otica.oticaapi.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String name);
    Boolean existsByName(String name);
    Boolean existsByModel(String model);

    boolean existsByNameAndModel(String name, String model);

    Product findByNameAndModel(String name, String model);

}