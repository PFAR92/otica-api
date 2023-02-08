package com.otica.oticaapi.service.product;

import com.otica.oticaapi.model.product.Product;
import com.otica.oticaapi.repository.product.ProductRepository;
import com.otica.oticaapi.service.exceptions.CustonException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product searchId (Product product){
        log.info("Solicitou busca de produto por id, id digitado: " + product.getId());
        existsProduct(product.getId());
        log.info("Produto com o id: " + product.getId()+ " encontrado");
        return productRepository.findById(product.getId()).get();
    }

    public List<Product> searchNames(Product product){
        log.info("Solicitou busca por nome do produto, busca digitada: " + product.getName());
        return productRepository.findByNameContaining(product.getName());
    }

    public List<Product> list(){
        log.info("Solicitou a busca de todos os produtos");
        return productRepository.findAll();
    }

    public void save(Product product) {
        log.info("Solicitou cadastrar um novo produto");
        productRepository.save(product);
        log.info("Produto cadastrado com sucesso!");
    }

    public void delete(Product product){
        log.info("Solicitou deletar o produto com o id: " + product.getId());
        existsProduct(product.getId());
        try {
            productRepository.deleteById(product.getId());
            log.info("Produto com o id: " + product.getId() + ", deletado com sucesso!");
        } catch (Exception e){
            throw new CustonException("Esse produto nao pode ser deletado pois ele consta em outros registros como compras ou" +
                    " vendas e isso faria seu registro ficar incompleto, favor deletar primeiro o registro");
        }
    }



    public void existsProduct (Long id){
        if (!productRepository.existsById(id)){
            throw new CustonException("O produto com id "+id+", nao existe");
        }
    }
}
