package com.otica.oticaapi.service.people;

import java.util.List;

import com.otica.oticaapi.repository.address.AddressRepository;
import com.otica.oticaapi.service.address.AddressService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otica.oticaapi.service.exceptions.NotFoundException;
import com.otica.oticaapi.model.people.Provider;
import com.otica.oticaapi.repository.people.ProviderRepository;

@Service
@Log4j2
public class ProviderService{
    
    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressService addressService;


    
    public Provider searchId(Provider provider) {
        log.info("Solicitou busca do Fornecedor por ID, ID digitado: " + provider.getId());
        if (!providerRepository.existsById(provider.getId())){
            throw new NotFoundException("Esse Fornecedor nao existe");
        } else {
            log.info("Fornecedor encontrado");
            return providerRepository.findById(provider.getId()).get();
        }
    }

    public Provider searchCnpj(Provider provider){
        log.info("Solicitou busca por CNPJ do Fornecedor, CNPJ digitado: " + provider.getCnpj());
        if (!providerRepository.existsByCnpj(provider.getCnpj())){
            throw new NotFoundException("Esse Fornecedor nao existe!");
        } else {
            log.info("Fornecedor encontrado");
            return providerRepository.findByCnpj(provider.getCnpj()).get();
        }
    }

    public List<Provider> searchNames(Provider provider){
        log.info("Solicitou busca por nome do Fornecedor, Nome digitado: " + provider.getName());
        return providerRepository.findByNameContaining(provider.getName());
    }

    
    public List<Provider> list() {
        log.info("Solicitou busca por todos os fornecedores");
        return providerRepository.findAll();
    }

    
    public ResponseEntity<Provider> save(Provider provider) {

        log.info("Solicitou cadastro de novo fornecedor");
        if (providerRepository.existsByCnpj(provider.getCnpj())) {
            throw new NotFoundException("Cnpj ja cadastrado");
        } else {
            String cep = provider.getAddress().getCep();
            provider.setAddress(addressService.addressCep(cep));
            if (!addressRepository.existsById(cep)){
                addressRepository.save(provider.getAddress());
            }
            log.info("Fornecedor cadastrado com sucesso!");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(providerRepository.save(provider));
        }
    }

    
    public ResponseEntity<Provider> alteration(Provider provider) {
        log.info("Solicitou alterar o Fornecedor com o ID: " + provider.getId());
        if(!providerRepository.existsById(provider.getId())) {
            throw new NotFoundException("Esse Fornecedor nao existe!");
        }
        Provider providerAlteration = providerRepository.findById(provider.getId()).get();
        if (providerRepository.existsByCnpj(provider.getCnpj()) && !providerAlteration.getCnpj().equals(provider.getCnpj())){
            throw new NotFoundException("Esse Cnpj ja esta cadastrado em um Fornecedor ativo");
        } else {
            log.info("Fornecedor com ID: " + provider.getId() + ", alterado com sucesso!");
            provider.setAddress(addressService.addressCep(provider.getAddress().getCep()));
            if (!addressRepository.existsById(provider.getAddress().getCep())){
                addressRepository.save(provider.getAddress());
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(providerRepository.save(provider));
        }
    }

    
    public void delete(Provider provider) {

        log.info("Solicitou excluir Fornecedor com o ID: " + provider.getId());
        if(!providerRepository.existsById(provider.getId())) {
            throw new NotFoundException("Esse Fornecedor nao existe!");
        } else {
            log.info("Fornecedor com o ID: " + provider.getId() + " excluido com sucesso!");
            providerRepository.deleteById(provider.getId());
            ResponseEntity.noContent().build();
        }
    }
}
