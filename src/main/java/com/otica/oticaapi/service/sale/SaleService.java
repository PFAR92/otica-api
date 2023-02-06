package com.otica.oticaapi.service.sale;

import com.otica.oticaapi.model.product.Product;
import com.otica.oticaapi.model.sale.Sale;
import com.otica.oticaapi.model.sale.Sale_Product;
import com.otica.oticaapi.repository.people.ClientRepository;
import com.otica.oticaapi.repository.product.ProductRepository;
import com.otica.oticaapi.repository.sale.SaleRepository;
import com.otica.oticaapi.repository.sale.Sale_ProductRepository;
import com.otica.oticaapi.service.exceptions.CustonException;
import com.otica.oticaapi.service.product.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Log4j2
@Service
public class SaleService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private Sale_ProductRepository sale_productRepository;

    @Autowired
    private ClientRepository clientRepository;

    public Sale searchId(Sale sale) {
        log.info("Solicitou busca de vendas por id, id digitado:" + sale.getId());
        if (!saleRepository.existsById(sale.getId())){
            throw new CustonException("essa venda nao existe!");
        }
        Sale saleId = saleRepository.findById(sale.getId()).get();
        for (Sale_Product product : saleId.getProducts()){
            product.getProduct().setQuantity(product.getOriginalQuantity());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(saleId).getBody();
    }

    public ResponseEntity<List<Sale>> searchMonth(Sale sale) {
        log.info("Solicitou a busca de vendas por mes: " + sale.getDate().getMonthValue()+"/"+sale.getDate().getYear());
        List<Sale> searchMonth = saleRepository.searchMonth(sale.getDate().getYear(), sale.getDate().getMonthValue());
        for (Sale sale1 : searchMonth){
            for (Sale_Product product : sale1.getProducts()){
                product.getProduct().setQuantity(product.getOriginalQuantity());
            }
        }
        return ResponseEntity.ok().body(searchMonth);
    }

    public List<Sale> list() {
        log.info("Solicitou busca de vendas");
        List<Sale> sales = saleRepository.findAll();
        for (Sale sale : sales){
            for (Sale_Product saleProduct : sale.getProducts()){
                saleProduct.getProduct().setQuantity(saleProduct.getOriginalQuantity());
            }
        }
        return sales;
    }

    public ResponseEntity<Sale> save(Sale sale) {
        log.info("Solicitou cadastrar uma nova venda");
        //verifica se o cliente ja existe no banco de dados
        try {
            sale.setClient(clientRepository.findByCpf(sale.getClient().getCpf()).get());
        } catch (NoSuchElementException ex){
            throw new CustonException("Esse Cliente nao existe, favor cadastrar antes, e passar o cpf correto");
        }
        //se a data não for preenchida será usada a data atual
        if (sale.getDate() == null){
            sale.setDate(LocalDate.now());
        }

        BigDecimal fullValue = BigDecimal.valueOf(0.0);
        BigDecimal totalValue = BigDecimal.valueOf(0.0);
        List<Sale_Product> saleProducts = new ArrayList<>();

        //percorrer os produtos da venda
        for (Sale_Product saleProduct : sale.getProducts()){
            Product product = saleProduct.getProduct();
            Sale_Product sale_productSave = new Sale_Product();
            sale_productSave.setOriginalQuantity(product.getQuantity());
            sale_productSave.setSale(sale);

            //o produto é recuperado do banco de dados
            if (productRepository.existsByNameAndModel(product.getName(), product.getModel())){
                Product productSave = productRepository.findByNameAndModel(product.getName(), product.getModel());
                product.setId(productSave.getId());
                product.setDescription(productSave.getDescription());
                product.setValue(productSave.getValue());
                product.setPurchase_price(productSave.getPurchase_price());

                //atualiza a quantidade no banco de dados
                boolean quantity = productSave.getQuantity() - sale_productSave.getOriginalQuantity() < 0;
                if (!quantity){
                    productSave.setQuantity(productSave.getQuantity() - sale_productSave.getOriginalQuantity());
                    productService.alteration(productSave);
                } else {
                    throw new CustonException("a quantidade da venda é maior que a quantidade em estoque, favor atualizar");
                }

                sale_productSave.setProduct(product);

            } else {
                throw new CustonException("Esse produto:" + product.getName() + ", nao existe, favor cadastrar;");
            }

            //soma o valor dos produtos e adiciona ao valor final
            fullValue = fullValue.add(sale_productSave.getProduct().getValue()
                    .multiply(BigDecimal.valueOf(sale_productSave.getOriginalQuantity())));

            //se tiver desconto aplica ao valor total
            boolean discount = sale.getDiscount().equals(0) || sale.getDiscount() == null;
            if(!discount){
                BigDecimal value = fullValue.multiply(BigDecimal.valueOf(sale.getDiscount()));
                BigDecimal totalDiscount = value.divideToIntegralValue(BigDecimal.valueOf(100));
                totalValue = fullValue.subtract(totalDiscount);

            } else {
                totalValue = fullValue;
            }

            saleProducts.add(sale_productSave);
        }

        sale.setFullValue(fullValue);
        sale.setTotalValue(totalValue);
        sale.setProducts(saleProducts);
        saleRepository.save(sale);
        sale_productRepository.saveAll(saleProducts);
        log.info("Venda salva com sucesso");
        return ResponseEntity.ok().body(sale);
    }


    public ResponseEntity<Sale> alteration(Sale sale) {
        log.info("Solicitou alterar a venda com o id:" + sale.getId());
        if (!saleRepository.existsById(sale.getId())) {
            throw new CustonException("essa venda nao existe!");
        }
        //verifica se o cliente ja existe no banco de dados
        try {
            sale.setClient(clientRepository.findByCpf(sale.getClient().getCpf()).get());
        } catch (NoSuchElementException ex) {
            throw new CustonException("Esse Cliente nao existe, favor cadastrar antes, e passar o cpf correto");
        }

        BigDecimal fullValue = BigDecimal.valueOf(0.0);
        BigDecimal totalValue = BigDecimal.valueOf(0.0);
        List<Sale_Product> saleProducts = new ArrayList<>();

        //analisa os produtos da venda a ser alterada
        Sale saleOrigin = saleRepository.findById(sale.getId()).get();
        for (Sale_Product saleProduct : saleOrigin.getProducts()) {

            //retida a quantidade dos produtos e atualiza o banco de dados para a venda ser alterada
            Product productSave = productRepository.findByNameAndModel(saleProduct.getProduct().getName(),
                    saleProduct.getProduct().getModel());
            productSave.setQuantity(productSave.getQuantity() + saleProduct.getOriginalQuantity());
            productService.alteration(productSave);

            //se um produto foi retirado da lista ele é deletado da venda
            boolean isPresent = sale.getProducts().contains(saleProduct);
            if (!isPresent){
                sale_productRepository.deleteById(saleProduct.getId());
            }
        }

        //percorrer os produtos da venda
        for (Sale_Product saleProduct : sale.getProducts()){
            Product product = saleProduct.getProduct();
            Sale_Product sale_productSave = new Sale_Product();
            sale_productSave.setOriginalQuantity(product.getQuantity());
            sale_productSave.setSale(sale);

            //o produto é recuperado do banco de dados
            if (productRepository.existsByNameAndModel(product.getName(), product.getModel())){
                Product productSave = productRepository.findByNameAndModel(product.getName(), product.getModel());
                product.setId(productSave.getId());
                product.setDescription(productSave.getDescription());
                product.setValue(productSave.getValue());
                product.setPurchase_price(productSave.getPurchase_price());

                //atualiza a quantidade no banco de dados
                boolean quantity = productSave.getQuantity() - sale_productSave.getOriginalQuantity() < 0;
                if (!quantity){
                    productSave.setQuantity(productSave.getQuantity() - sale_productSave.getOriginalQuantity());
                    productService.alteration(productSave);
                } else {
                    throw new CustonException("a quantidade da venda é maior que a quantidade em estoque, favor atualizar");
                }

                sale_productSave.setProduct(product);

            } else {
                throw new CustonException("Esse produto:" + product.getName() + ", nao existe, favor cadastrar;");
            }

            //soma o valor dos produtos e adiciona ao valor final
            fullValue = fullValue.add(sale_productSave.getProduct().getValue()
                    .multiply(BigDecimal.valueOf(sale_productSave.getOriginalQuantity())));

            //se tiver desconto aplica ao valor total
            boolean discount = sale.getDiscount().equals(0) || sale.getDiscount() == null;
            if(!discount){
                BigDecimal value = fullValue.multiply(BigDecimal.valueOf(sale.getDiscount()));
                BigDecimal totalDiscount = value.divideToIntegralValue(BigDecimal.valueOf(100));
                totalValue = fullValue.subtract(totalDiscount);

            } else {
                totalValue = fullValue;
            }

            saleProducts.add(sale_productSave);
        }

        sale.setFullValue(fullValue);
        sale.setTotalValue(totalValue);
        sale.setProducts(saleProducts);
        saleRepository.save(sale);
        sale_productRepository.saveAll(saleProducts);
        log.info("Venda salva com sucesso");
        return ResponseEntity.ok().body(sale);
    }

    public void delete(Sale sale) {
        log.info("Solicitou deletar a venda com id:" + sale.getId());
        if (!saleRepository.existsById(sale.getId())){
            throw new CustonException("Essa venda nao existe!");
        }
        Sale saleDelete = saleRepository.findById(sale.getId()).get();
         //retornar a quantidade de produtos da venda deletada para o banco de dados
        for (Sale_Product product : saleDelete.getProducts()){
            Product productReturn = productRepository.findById(product.getProduct().getId()).get();
            productReturn.setQuantity(productReturn.getQuantity() + product.getOriginalQuantity());
            productService.alteration(productReturn);
            sale_productRepository.delete(product);
        }
        saleRepository.deleteById(sale.getId());
        log.info("Venda com o id:" + sale.getId() + ", deletada com sucesso!");
        ResponseEntity.noContent().build();
    }
}
