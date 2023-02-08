package com.otica.oticaapi.controller.product;

import com.otica.oticaapi.model.product.Product;
import com.otica.oticaapi.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/id")
    @ResponseStatus(HttpStatus.OK)
    public Product searchId(@RequestBody Product product){
        return productService.searchId(product);
    }

    @GetMapping(value = "/names")
    @ResponseStatus(HttpStatus.OK)
    public List<Product> searchNames(@RequestBody Product product){
        return productService.searchNames(product);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> list(){
        return productService.list();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Product product){
        productService.delete(product);
    }
}
