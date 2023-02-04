package com.otica.oticaapi.service.address;

import com.otica.oticaapi.model.address.Address;
import com.otica.oticaapi.repository.address.AddressRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressCepConsult addressCepConsult;


    public Address addressCep(String cep){
        return addressCepConsult.addressCep(cep);
    }

    public void thisAddressDoesNotExist(Address address){
        if (!addressRepository.existsById(address.getCep())){
            addressRepository.save(address);
            log.info("endereço salvo");
        }
    }
}
