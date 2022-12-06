package com.otica.oticaapi.controller.people;

import org.springframework.http.ResponseEntity;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.otica.oticaapi.model.people.Provider;
import com.otica.oticaapi.service.people.ProviderService;

@RestController
@RequestMapping("/people/provider")
public class ProviderController {
    
    @Autowired
    private ProviderService providerService;

    @GetMapping("/id")
    public Provider searchId(@RequestBody Provider provider){
        return providerService.searchId(provider);
    }

    @GetMapping(value = "/cnpj")
    public Provider searchCnpj(@RequestBody Provider provider){
        return providerService.searchCnpj(provider);
    }

    @GetMapping(value = "/names")
    public List<Provider> searchNames(@RequestBody Provider provider){
        return providerService.searchNames(provider);
    }

    @GetMapping
    public List<Provider> list(){
        return providerService.list();
    }

    @PostMapping
    public Provider save (@RequestBody @Valid Provider provider){
        return providerService.save(provider);
    }

    @PutMapping
    public ResponseEntity<Provider> alteration(@RequestBody @Valid Provider provider){
        return providerService.alteration(provider);
    }

    @DeleteMapping
    public void delete (@RequestBody Provider provider){
        providerService.delete(provider);
    }
}
