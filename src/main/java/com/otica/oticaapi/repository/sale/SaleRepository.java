package com.otica.oticaapi.repository.sale;

import com.otica.oticaapi.model.sale.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM sale WHERE YEAR(date) = (:year) AND MONTH(date) = (:month)")
    List<Sale> searchMonth(int year, int month);
}