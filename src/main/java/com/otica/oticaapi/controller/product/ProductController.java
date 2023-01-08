package com.otica.oticaapi.controller.product;

import com.otica.oticaapi.model.product.Product;
import com.otica.oticaapi.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/product/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/id")
    public Product searchId(@RequestBody Product product){
        return productService.searchId(product);
    }

    @GetMapping(value = "/names")
    public List<Product> searchNames(@RequestBody Product product){
        return productService.searchNames(product);
    }

    @GetMapping
    public List<Product> list(){
        return productService.list();
    }

    @PostMapping
    public ResponseEntity<Product> save(@RequestBody @Valid Product product){
        return productService.save(product);
    }

    @PutMapping
    public ResponseEntity<Product> alteration(@RequestBody @Valid Product product){
        return productService.alteration(product);
    }

    @DeleteMapping
    public void delete(@RequestBody Product product){
        productService.delete(product);
    }
}
