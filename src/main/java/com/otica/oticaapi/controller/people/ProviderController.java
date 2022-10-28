package com.otica.oticaapi.controller.people;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otica.oticaapi.model.people.Provider;
import com.otica.oticaapi.repository.people.ProviderRepository;

@RestController
@RequestMapping("/people/provider")
public class ProviderController {
    
    @Autowired
    private ProviderRepository providerRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Provider>> searchId(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(providerRepository.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Provider>> list(){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(providerRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Provider> save(@RequestBody Provider provider){
        return ResponseEntity.status(HttpStatus.CREATED).body(providerRepository.save(provider));
    }

    @PutMapping
    public ResponseEntity<Provider> alteration(@RequestBody Provider provider){
        if(providerRepository.findById(provider.getId()).isPresent())
            return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(providerRepository.save(provider));
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Provider provider){
        try {
            providerRepository.deleteById(provider.getId());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
             
    }
}
