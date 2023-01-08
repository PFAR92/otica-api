package com.otica.oticaapi.controller.people;

import java.util.List;
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
    public Client searchId(@RequestBody Client client){
        return clientService.searchId(client);
    }

    @GetMapping(value = "/cpf")
    public Client searchCpf (@RequestBody Client client){
        return clientService.searchCpf(client);
    }

    @GetMapping(value = "/names")
    public List<Client> searchNames (@RequestBody Client client){
        return clientService.searchNames(client);
    }

    @GetMapping
    public List<Client> list(){
        return clientService.list();
    }

    @PostMapping
    public ResponseEntity<Client> save (@RequestBody @Valid Client client){
        return clientService.save(client);
    }

    @PutMapping
    public ResponseEntity<Client> alteration(@RequestBody @Valid Client client){
        return clientService.alteration(client);
    }

    @DeleteMapping
    public void delete (@RequestBody Client client){
        clientService.delete(client);
    }
}
