package com.otica.oticaapi.service.people;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otica.oticaapi.service.exceptions.NotFoudException;
import com.otica.oticaapi.model.people.Provider;
import com.otica.oticaapi.repository.people.ProviderRepository;

@Service
public class ProviderService{
    
    @Autowired
    private ProviderRepository providerRepository;

    
    public Provider searchId(Long id) {
        return providerRepository.findById(id).orElseThrow(() -> new NotFoudException("Esse fornecedor nao existe!"));
    }

    public Provider searchCnpj(String cnpj){
        return providerRepository.findByCnpj(cnpj).orElseThrow(() -> new NotFoudException("Cnpj nao encontrado"));
    }

    public Provider searchName(String name){
        return providerRepository.findByName(name).orElseThrow(() -> new NotFoudException("Esse fornecedor nao existe"));
    }

    public List<Provider> searchNames(String name){
        return providerRepository.findByNameContaining(name);
    }

    
    public List<Provider> list() {
        return providerRepository.findAll();
    }

    
    public Provider save(Provider provider) {
        if (providerRepository.existsByCnpj(provider.getCnpj()))
            throw new NotFoudException("Cnpj ja cadastrado");
        return providerRepository.save(provider);
    }

    
    public ResponseEntity<Provider> alteration(Provider provider) {
        if(!providerRepository.existsById(provider.getId()))
            throw new NotFoudException("Esse Fornecedor nao existe!");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(providerRepository.save(provider));
    }

    
    public void delete(Long id) {
        if(!providerRepository.existsById(id))
            throw new NotFoudException("Esse Fornecedor nao existe!");
        providerRepository.deleteById(id);
        ResponseEntity.noContent().build();
        
    }
}
