package com.otica.oticaapi.service.address;

import com.otica.oticaapi.model.address.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
public interface AddressService {

    @GetMapping("/{cep}/json/")
    Address addressCep(@PathVariable("cep") String cep);

}
