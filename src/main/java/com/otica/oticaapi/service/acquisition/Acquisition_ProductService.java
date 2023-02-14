package com.otica.oticaapi.service.acquisition;

import com.otica.oticaapi.model.acquisition.Acquisition;
import com.otica.oticaapi.model.acquisition.Acquisition_Product;
import com.otica.oticaapi.model.product.Product;
import com.otica.oticaapi.repository.acquisition.Acquisition_ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Acquisition_ProductService {

    @Autowired
    private Acquisition_ProductRepository acquisition_productRepository;

    public Acquisition_Product save (Product product, Acquisition acquisition){
        Acquisition_Product acquisitionProduct = new Acquisition_Product();
        acquisitionProduct.setAcquisition(acquisition);
        acquisitionProduct.setProduct(product);
        acquisitionProduct.setOriginalQuantity(product.getQuantity());
        return acquisitionProduct;
    }

    public void saveAll (List<Acquisition_Product> acquisition_products){
        acquisition_productRepository.saveAll(acquisition_products);
    }

    public void delete (Long id) {
        acquisition_productRepository.deleteById(id);
    }
}
