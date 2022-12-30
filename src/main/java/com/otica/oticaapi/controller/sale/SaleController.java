package com.otica.oticaapi.controller.sale;

import com.otica.oticaapi.model.sale.Sale;
import com.otica.oticaapi.service.sale.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping(value = "/id")
    private Sale searchId(@RequestBody Sale sale){
       return saleService.searchId(sale);
    }

    @GetMapping(value = "/month")
    private ResponseEntity<List<Sale>> searchMonth(@RequestBody Sale sale){
        return saleService.searchMonth(sale);
    }

    @GetMapping
    private List<Sale> list(){
        return saleService.list();
    }

    @PostMapping
    private ResponseEntity<Sale> save(@RequestBody Sale sale){
        return saleService.save(sale);
    }

    @PutMapping
    private ResponseEntity<Sale> alteration(@RequestBody Sale sale){
        return saleService.alteration(sale);
    }

    @DeleteMapping
    private void delete(@RequestBody Sale sale){
        saleService.delete(sale);
    }
}
