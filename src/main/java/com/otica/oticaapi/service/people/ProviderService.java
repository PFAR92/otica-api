package com.otica.oticaapi.service.people;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otica.oticaapi.exceptions.NotFoudException;
import com.otica.oticaapi.model.people.Provider;
import com.otica.oticaapi.repository.people.ProviderRepository;

@Service
public class ProviderService{
    
    @Autowired
    private ProviderRepository providerRepository;

    
    public Provider searchId(Long id) {
        return providerRepository.findById(id).orElseThrow(() -> new NotFoudException("Esse fornecedor não existe!"));
    }

    
    public List<Provider> list() {
        return providerRepository.findAll();
    }

    
    public Provider save(Provider provider) {
        return providerRepository.save(provider);
    }

    
    public ResponseEntity<Provider> alteration(Provider provider) {
        if(!providerRepository.existsById(provider.getId()))
            throw new NotFoudException("Esse Fornecedor não existe!");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(providerRepository.save(provider));
    }

    
    public ResponseEntity<Provider> delete(Provider provider) {
        if(!providerRepository.existsById(provider.getId()))
            throw new NotFoudException("Esse Fornecedor não existe!");
        providerRepository.deleteById(provider.getId());
        return ResponseEntity.noContent().build();
        
    }
}
