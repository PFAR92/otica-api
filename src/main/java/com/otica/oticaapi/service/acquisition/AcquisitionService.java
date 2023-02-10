package com.otica.oticaapi.service.acquisition;

import com.otica.oticaapi.model.acquisition.Acquisition;
import com.otica.oticaapi.model.acquisition.Acquisition_Product;
import com.otica.oticaapi.model.product.Product;
import com.otica.oticaapi.repository.acquisition.AcquisitionRepository;
import com.otica.oticaapi.repository.acquisition.Acquisition_ProductRepository;
import com.otica.oticaapi.repository.people.ProviderRepository;
import com.otica.oticaapi.repository.product.ProductRepository;
import com.otica.oticaapi.service.exceptions.CustonException;
import com.otica.oticaapi.service.people.ProviderService;
import com.otica.oticaapi.service.product.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Log4j2
@Service
@AllArgsConstructor
public class AcquisitionService {

    private ProviderService providerService;
    private ProviderRepository providerRepository;
    private AcquisitionRepository acquisitionRepository;
    private ProductService productService;
    private ProductRepository productRepository;
    private Acquisition_ProductRepository acquisition_productRepository;


    public Acquisition searchId(Acquisition acquisition) {
        log.info("Solicitou busca de compra por id, id digitado: " + acquisition.getId());
        existsAcquisition(acquisition.getId());
        log.info("Compra com o id:" + acquisition.getId() + ", encontrado");
        return searchWithTheOriginalQuantity(acquisition);
    }
    public List<Acquisition> searchMonth(Acquisition acquisition) {
        log.info("Solicitou a busca de compras do mes:" + acquisition.getDate().getMonthValue()+"/"+acquisition.getDate().getYear());
        return searchWithTheOriginalQuantity(acquisitionRepository.searchMonth(acquisition.getDate()
                .getYear(), acquisition.getDate().getMonthValue()));
    }

    public List<Acquisition> list() {

        return searchWithTheOriginalQuantity(acquisitionRepository.findAll());
    }
    @Transactional
    public Acquisition save(Acquisition acquisition) {
        log.info("Solicitou cadastrar uma nova compra");
        if (providerService.providerDoesNotExist(acquisition.getProvider().getCnpj())){
            providerService.save(acquisition.getProvider());
        }
        acquisition.setProvider(providerService.searchCnpj(acquisition.getProvider()));
        if (acquisitionRepository.existsByDateAndProvider(acquisition.getDate(), acquisition.getProvider())){
            throw new CustonException("Ja existe uma compra cadastrada com essa data e fornecedor, favor atualizar a compra existente!");
        }
        //se a data não for preenchida será usada a data atual
        if (acquisition.getDate() == null){
            acquisition.setDate(LocalDate.now());
        }
        List<Acquisition_Product> acquisition_products = new ArrayList<>();
        BigDecimal fullValue = BigDecimal.valueOf(0.0);
        //Analisa todos os produtos da compra
        for (Acquisition_Product productList : acquisition.getProducts()){
            Product product = productList.getProduct();
            Acquisition_Product acquisition_product = new Acquisition_Product();
            if (productRepository.existsByNameAndModel(product.getName(), product.getModel())){
                product.setId(productRepository.findByNameAndModel(product.getName(), product.getModel()).getId());
                acquisition_product.setAcquisition(acquisition);
                acquisition_product.setProduct(product);
                acquisition_product.setOriginalQuantity(product.getQuantity());
                acquisition_products.add(acquisition_product);

                //se o produto ja existir aumenta a quantidade no banco de dados
                Product productAlteration = productRepository.findById(product.getId()).get();
                productAlteration.setQuantity(productAlteration.getQuantity() + product.getQuantity());
                productService.save(productAlteration);

                //calcula o valor total da compra através do valor dos produtos
                BigDecimal value = acquisition_product.getProduct().getPurchase_price()
                        .multiply(BigDecimal.valueOf(acquisition_product.getOriginalQuantity()));
                fullValue = fullValue.add(value);

            } else {
                //se o produto não existir ele é salvo
                productRepository.save(product);
                Product productSave = productRepository.findByNameAndModel(product.getName(), product.getModel());
                acquisition_product.setAcquisition(acquisition);
                acquisition_product.setProduct(productSave);
                acquisition_product.setOriginalQuantity(product.getQuantity());
                acquisition_products.add(acquisition_product);

                //calcula o valor total da compra através do valor dos produtos
                BigDecimal value = product.getPurchase_price().multiply(BigDecimal.valueOf(product.getQuantity()));
                fullValue = fullValue.add(value);

            }
        }
        acquisition.setFullValue(fullValue);
        acquisition.setProducts(acquisition_products);
        acquisitionRepository.save(acquisition);
        acquisition_productRepository.saveAll(acquisition_products);
        return acquisition;
    }

    public ResponseEntity<Acquisition> alteration(Acquisition acquisition) {
        log.info("Solicitou alterar a compra com id: " + acquisition.getId());
        if (!acquisitionRepository.existsById(acquisition.getId())){
            throw new CustonException("Essa compra nao existe");
        }
        if (!providerRepository.existsByCnpj(acquisition.getProvider().getCnpj())){
            throw new CustonException("Esse Fornecedor nao existe, favor cadastrar antes");
        }
        //verifica se o usuário não alterou para um compra com o mesmo fornecedor e data de alguma compra que ja existe
        Acquisition acquisitionAlteration = acquisitionRepository.findById(acquisition.getId()).get();
        boolean validation = !acquisitionAlteration.getDate().equals(acquisition.getDate()) && acquisitionAlteration.getProvider()
                                .equals(acquisition.getProvider());
        if (validation && acquisitionRepository.existsByDateAndProvider(acquisition.getDate(), acquisition.getProvider())){
            throw new CustonException("Ja existe outra compra salva com essa data e fornecedor, favor atualizar a compra correta");
        }

        acquisition.setProvider(providerRepository.findByCnpj(acquisition.getProvider().getCnpj()).get());
        List<Acquisition_Product> acquisition_products = new ArrayList<>();
        BigDecimal fullValue = BigDecimal.valueOf(0.0);

        //Analisa todos os produtos da compra a ser alterada
        for (Acquisition_Product acquisitionProduct : acquisitionAlteration.getProducts()){

            //retira a quantidade dos produtos do banco de dados para a compra ser alterada
            Product productSave = productRepository.findByNameAndModel(acquisitionProduct.getProduct().getName()
                    , acquisitionProduct.getProduct().getModel());
            productSave.setQuantity(productSave.getQuantity() - acquisitionProduct.getOriginalQuantity());
            productService.save(productSave);

            //se um produto foi retirado ele é deletado da compra
            boolean isPresent = acquisition.getProducts().contains(acquisitionProduct);
            if (!isPresent){
                acquisition_productRepository.deleteById(acquisitionProduct.getId());
            }
        }

        //Analisa todos os produtos da compra alterada
        for (Acquisition_Product productList : acquisition.getProducts()){
            Product product = productList.getProduct();
            Acquisition_Product acquisition_product = new Acquisition_Product();


            if (productRepository.existsByNameAndModel(product.getName(), product.getModel())){
                product.setId(productRepository.findByNameAndModel(product.getName(), product.getModel()).getId());
                acquisition_product.setAcquisition(acquisition);
                acquisition_product.setProduct(product);
                acquisition_product.setOriginalQuantity(product.getQuantity());
                acquisition_products.add(acquisition_product);

                //se o produto ja existir aumenta a quantidade no banco de dados
                Product productAlteration = productRepository.findById(product.getId()).get();
                productAlteration.setQuantity(productAlteration.getQuantity() + product.getQuantity());
                productService.save(productAlteration);

            //calcula o valor total da compra através do valor dos produtos
            BigDecimal value = acquisition_product.getProduct().getPurchase_price()
                    .multiply(BigDecimal.valueOf(acquisition_product.getOriginalQuantity()));
            fullValue = fullValue.add(value);

            } else {
                //se o produto não existir ele é salvo
                productRepository.save(product);
                Product productSave = productRepository.findByNameAndModel(product.getName(), product.getModel());
                acquisition_product.setAcquisition(acquisition);
                acquisition_product.setProduct(productSave);
                acquisition_product.setOriginalQuantity(product.getQuantity());
                acquisition_products.add(acquisition_product);

                //calcula o valor total da compra através do valor dos produtos
                BigDecimal value = product.getPurchase_price().multiply(BigDecimal.valueOf(product.getQuantity()));
                fullValue = fullValue.add(value);

            }
        }
        acquisition.setFullValue(fullValue);
        acquisition.setProducts(acquisition_products);
        acquisitionRepository.save(acquisition);
        acquisition_productRepository.saveAll(acquisition_products);
        log.info("Compra alterada com sucesso");
        return ResponseEntity.ok().body(acquisition);
    }

    public void delete(Acquisition acquisition) {
        log.info("Solicitou deletar compra com o id:" + acquisition.getId());
        if (!acquisitionRepository.existsById(acquisition.getId())){
            throw new CustonException("Essa compra nao existe!");
        }
        //atualizar a quantidade dos produtos da compra deletada
        Acquisition acquisitionDelete = acquisitionRepository.findById(acquisition.getId()).get();
        for (Acquisition_Product acquisitionProduct : acquisitionDelete.getProducts()){
            Product product = productRepository.findById(acquisitionProduct.getProduct().getId()).get();
            product.setQuantity(product.getQuantity() - acquisitionProduct.getOriginalQuantity());
            productService.save(product);
            acquisition_productRepository.delete(acquisitionProduct);
        }

        acquisitionRepository.delete(acquisition);
        log.info("compra com o id:" + acquisition.getId() + ", deletado com sucesso");
        ResponseEntity.noContent().build();
    }





    public void existsAcquisition (Long id){
        if (!acquisitionRepository.existsById(id)){
            throw new CustonException("A compra com o id "+id+", nao existe");
        }
    }

    public Acquisition searchWithTheOriginalQuantity (Acquisition acquisition){
        Acquisition acquisitionAlteration = acquisitionRepository.findById(acquisition.getId()).get();
        for (Acquisition_Product product : acquisitionAlteration.getProducts()){
            product.getProduct().setQuantity(product.getOriginalQuantity());
        }
        return acquisitionAlteration;
    }

    public List<Acquisition> searchWithTheOriginalQuantity (List<Acquisition> acquisitions){
        for (Acquisition acquisition : acquisitions){
            for (Acquisition_Product product : acquisition.getProducts()){
                product.getProduct().setQuantity(product.getOriginalQuantity());
            }
        }
        return acquisitions;
    }
}
