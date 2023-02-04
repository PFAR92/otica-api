package com.otica.oticaapi.service.product;

import com.otica.oticaapi.model.product.Product;
import com.otica.oticaapi.repository.address.AddressRepository;
import com.otica.oticaapi.repository.people.ProviderRepository;
import com.otica.oticaapi.repository.product.ProductRepository;
import com.otica.oticaapi.service.address.AddressCepConsult;
import com.otica.oticaapi.service.exceptions.NotFoundException;
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
    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressCepConsult addressCepConsult;

    public Product searchId (Product product){
        log.info("Solicitou busca de produto por id, id digitado: " + product.getId());
        if (!productRepository.existsById(product.getId())){
            throw new NotFoundException("Esse produto nao existe!");
        } else {
            log.info("Produto com o id: " + product.getId()+ " encontrado");
            return productRepository.findById(product.getId()).get();
        }
    }

    public List<Product> searchNames(Product product){
        log.info("Solicitou busca por nome do produto, busca digitada: " + product.getName());
        return productRepository.findByNameContaining(product.getName());
    }

    public List<Product> list(){
        log.info("Solicitou a busca de todos os produtos");
        return productRepository.findAll();
    }

    public ResponseEntity<Product> save(Product product) {
        log.info("Solicitou cadastrar um novo produto");
        if (productRepository.existsByName(product.getName()) && productRepository.existsByModel(product.getModel())){
            throw new NotFoundException("ja existe um produto com esse Nome e Modelo cadastrado!");
        } else {
            log.info("Produto cadastrado com sucesso!");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(productRepository.save(product));
        }
    }

    public ResponseEntity<Product> alteration(Product product) {
        log.info("Solicitou alterar o Produto com o id: " + product.getId());
        if (!productRepository.existsById(product.getId())) {
            throw new NotFoundException("Esse produto nao existe!");
        } else {
            Product productAlteration = productRepository.findById(product.getId()).get();
            boolean verification = productAlteration.getName().equalsIgnoreCase(product.getName())
                    && productAlteration.getModel().equalsIgnoreCase(product.getModel());
            if (!verification && productRepository.existsByName(product.getName()) && productRepository.existsByModel(product.getModel())) {
                throw new NotFoundException("Esse nome e modelo ja existe em um produto que ja esta salvo");
            }
            log.info("Produto com o id: " + product.getId() + ", alterado com sucesso!");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(productRepository.save(product));

        }
    }

    public void delete(Product product){
        log.info("Solicitou deletar o produto com o id: " + product.getId());
        if (!productRepository.existsById(product.getId())){
            throw new NotFoundException("Esse produto nao existe");
        } else {
            try {
                productRepository.deleteById(product.getId());
            } catch (Exception e){
                throw new NotFoundException("Esse produto nao pode ser deletado pois ele consta em outros registros como compras ou" +
                        " vendas e isso faria seu registro ficar incompleto, favor deletar primeiro o registro");
            }
            log.info("Produto com o id: " + product.getId() + ", deletado com sucesso!");
            ResponseEntity.noContent().build();

        }

    }
}
