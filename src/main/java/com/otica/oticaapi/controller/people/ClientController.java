package com.otica.oticaapi.controller.people;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.otica.oticaapi.model.people.Client;
import com.otica.oticaapi.service.people.ClientService;

@RestController
@RequestMapping("/people/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/id")
    @ResponseStatus(HttpStatus.OK)
    public Client searchId(@RequestBody Client client){
        return clientService.searchId(client);
    }

    @GetMapping(value = "/cpf")
    @ResponseStatus(HttpStatus.OK)
    public Client searchCpf (@RequestBody Client client){
        return clientService.searchCpf(client);
    }

    @GetMapping(value = "/names")
    @ResponseStatus(HttpStatus.OK)
    public List<Client> searchNames (@RequestBody Client client){
        return clientService.searchNames(client);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Client> list(){
        return clientService.list();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client save (@RequestBody @Valid Client client){
        return clientService.save(client);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Client alteration(@RequestBody @Valid Client client){
        return clientService.alteration(client);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@RequestBody Client client){
        clientService.delete(client);
    }
}
