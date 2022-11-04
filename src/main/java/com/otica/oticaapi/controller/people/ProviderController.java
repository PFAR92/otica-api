package com.otica.oticaapi.controller.people;

import org.springframework.http.ResponseEntity;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
