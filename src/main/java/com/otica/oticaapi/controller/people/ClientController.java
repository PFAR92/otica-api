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

    @GetMapping(value = "/{id}")
    public Client searchId(@PathVariable Long id){
        return clientService.searchId(id);
    }

    @GetMapping(value = "/cpf")
    public Client searchCpf (@RequestParam(value = "cpf") String cpf){
        return clientService.searchCpf(cpf);
    }

    @GetMapping(value = "/name")
    public Client searchName (@RequestParam(value = "name") String name){
        return clientService.seachName(name);
    }

    @GetMapping(value = "/names")
    public List<Client> searchNames (@RequestParam(value = "name") String name){
        return clientService.searchNames(name);
    }

    @GetMapping
    public List<Client> list(){
        return clientService.list();
    }

    @PostMapping
    public Client save (@RequestBody @Valid Client client){
        return clientService.save(client);
    }

    @PutMapping
    public ResponseEntity<Client> alteration(@RequestBody @Valid Client client){
        return clientService.alteration(client);
    }

    @DeleteMapping(value = "/{id}")
    public void delete (@PathVariable Long id){
          clientService.delete(id);
    }
}
