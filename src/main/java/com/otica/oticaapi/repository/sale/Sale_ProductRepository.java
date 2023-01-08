package com.otica.oticaapi.repository.sale;

import com.otica.oticaapi.model.sale.Sale_Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Sale_ProductRepository extends JpaRepository<Sale_Product, Long> {
}