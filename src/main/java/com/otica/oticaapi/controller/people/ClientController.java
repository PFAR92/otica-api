package com.otica.oticaapi.controller.people;

import java.util.List;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otica.oticaapi.model.people.Client;
import com.otica.oticaapi.service.people.ClientService;

@RestController
@RequestMapping("/people/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/{id}")
    public Client searchId(@PathVariable Long id){
        return clientService.searchId(id);
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

    @DeleteMapping
    public void delete (@RequestBody Client client){
          clientService.delete(client);        
    }
}
