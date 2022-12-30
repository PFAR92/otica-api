package com.otica.oticaapi.repository.acquisition;

import com.otica.oticaapi.model.acquisition.Acquisition_Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Acquisition_ProductRepository extends JpaRepository<Acquisition_Product, Long> {

   Acquisition_Product findByProductNameAndProductModel(String productName, String productModel);

   Acquisition_Product findByAcquisitionIdAndProductId(Long acquisitionId, Long productId);

   boolean existsByProductNameAndProductModel(Long id, Long id1);
}

