package com.otica.oticaapi.service.people;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger();

    
    public Provider searchId(Provider provider) {
        logger.info("Solicitou busca do Fornecedor por ID, ID digitado: " + provider.getId());
        if (!providerRepository.existsById(provider.getId())){
            throw new NotFoudException("Esse Fornecedor nao existe");
        } else {
            logger.info("Fornecedor encontrado");
            return providerRepository.findById(provider.getId()).get();
        }
    }

    public Provider searchCnpj(Provider provider){
        logger.info("Solicitou busca por CNPJ do Fornecedor, CNPJ digitado: " + provider.getCnpj());
        if (!providerRepository.existsByCnpj(provider.getCnpj())){
            throw new NotFoudException("Esse Fornecedor nao existe!");
        } else {
            logger.info("Fornecedor encontrado");
            return providerRepository.findByCnpj(provider.getCnpj()).get();
        }
    }

    public List<Provider> searchNames(Provider provider){
        logger.info("Solicitou busca por nome do Fornecedor, Nome digitado: " + provider.getName());
        return providerRepository.findByNameContaining(provider.getName());
    }

    
    public List<Provider> list() {
        logger.info("Solicitou busca por todos os fornecedores");
        return providerRepository.findAll();
    }

    
    public Provider save(Provider provider) {

        logger.info("Solicitou cadastro de novo fornecedor");
        if (providerRepository.existsByCnpj(provider.getCnpj())) {
            throw new NotFoudException("Cnpj ja cadastrado");
        } else {
            logger.info("Fornecedor cadastrado com sucesso!");
            return providerRepository.save(provider);
        }
    }

    
    public ResponseEntity<Provider> alteration(Provider provider) {
        logger.info("Solicitou alterar o Fornecedor com o ID: " + provider.getId());
        if(!providerRepository.existsById(provider.getId())) {
            throw new NotFoudException("Esse Fornecedor nao existe!");
        } else {
            logger.info("Fornecedor com ID: " + provider.getId() + ", alterado com sucesso!");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(providerRepository.save(provider));
        }
    }

    
    public void delete(Provider provider) {

        logger.info("Solicitou excluir Fornecedor com o ID: " + provider.getId());
        if(!providerRepository.existsById(provider.getId())) {
            throw new NotFoudException("Esse Fornecedor nao existe!");
        } else {
            logger.info("Fornecedor com o ID: " + provider.getId() + " excluido com sucesso!");
            providerRepository.deleteById(provider.getId());
            ResponseEntity.noContent().build();
        }
    }
}
