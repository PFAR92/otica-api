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

    @GetMapping("/{id}")
    public Provider searchId(@PathVariable Long id){
        return providerService.searchId(id);
    }

    @GetMapping(value = "/cnpj")
    public Provider searchCnpj(@RequestParam(value = "cnpj") String cnpj){
        return providerService.searchCnpj(cnpj);
    }

    @GetMapping(value = "/name")
    public Provider searchName(@RequestParam(value = "name") String name){
        return providerService.searchName(name);
    }

    @GetMapping(value = "/names")
    public List<Provider> searchNames(@RequestParam(value = "name") String name){
        return providerService.searchNames(name);
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

    @DeleteMapping(value = "/{id}")
    public void delete (@PathVariable Long id){
        providerService.delete(id);
    }
}
