package com.otica.oticaapi.service.people;

import com.otica.oticaapi.model.people.Provider;
import com.otica.oticaapi.repository.people.ProviderRepository;
import com.otica.oticaapi.service.address.AddressService;
import com.otica.oticaapi.service.exceptions.CustonException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2

@AllArgsConstructor
public class ProviderService{


    private ProviderRepository providerRepository;
    private AddressService addressService;


    
    public Provider searchId(Provider provider) {
        log.info("Solicitou busca do Fornecedor por ID, ID digitado: " + provider.getId());
        existsProvider(provider.getId());
        return providerRepository.findById(provider.getId()).get();
    }

    public Provider searchCnpj(Provider provider){
        log.info("Solicitou busca por CNPJ do Fornecedor, CNPJ digitado: " + provider.getCnpj());
        existsProvider(provider.getCnpj());
        return providerRepository.findByCnpj(provider.getCnpj()).get();
    }

    public List<Provider> searchNames(Provider provider){
        log.info("Solicitou busca por nome do Fornecedor, Nome digitado: " + provider.getName());
        return providerRepository.findByNameContaining(provider.getName());
    }

    
    public List<Provider> list() {
        log.info("Solicitou busca por todos os fornecedores");
        return providerRepository.findAll();
    }

    
    public Provider save(Provider provider) {

        log.info("Solicitou cadastro de um novo fornecedor");
        providerCannotBeRegistered(provider);
        provider.setAddress(addressService.addressCep(provider.getAddress().getCep()));
        addressService.thisAddressDoesNotExist(provider.getAddress());
        log.info("Fornecedor cadastrado com sucesso!");

        return providerRepository.save(provider);

    }

    
    public Provider alteration(Provider provider) {
        log.info("Solicitou alterar o Fornecedor com o ID: " + provider.getId());
        existsProvider(provider.getId());
        providerCannotBeRegistered(provider);
        log.info("Fornecedor com ID: " + provider.getId() + ", alterado com sucesso!");
        provider.setAddress(addressService.addressCep(provider.getAddress().getCep()));
        addressService.thisAddressDoesNotExist(provider.getAddress());

        return providerRepository.save(provider);
    }

    
    public void delete(Provider provider) {

        existsProvider(provider.getId());
        providerRepository.deleteById(provider.getId());
        log.info("Fornecedor com o ID: " + provider.getId() + " excluido com sucesso!");

    }








    public void existsProvider (Long id){
        if (!providerRepository.existsById(id)){
            throw new CustonException("O fornecedor com o id " + id+ ", nao existe");
        } else {
            log.info("Fornecedor encontrado");
        }
    }

    public void existsProvider (String cnpj){
        if (!providerRepository.existsByCnpj(cnpj)){
            throw new CustonException("O fornecedor com o cnpj " +cnpj+ ", nao existe");
        } else {
            log.info("Fornecedor encontrado");
        }
    }

    public void providerCannotBeRegistered (Provider provider){
        if (provider.getId() == null || !providerRepository.existsById(provider.getId())){
            if (providerRepository.existsByCnpj(provider.getCnpj())){
                throw new CustonException("Ja existe um fornecedor com o cnpj "+provider.getCnpj()+", cadastrado");
            }
        } else {
            Provider providerAlteration = providerRepository.findById(provider.getId()).get();
            if (providerRepository.existsByCnpj(provider.getCnpj()) && !providerAlteration.getCnpj().equals(provider.getCnpj())){
                throw new CustonException("Existe outro fornecedor cadastrado com o cnpj "+provider.getCnpj());
            }
        }
    }
}
