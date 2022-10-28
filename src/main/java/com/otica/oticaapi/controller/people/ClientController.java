package com.otica.oticaapi.controller.people;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otica.oticaapi.model.people.Client;
import com.otica.oticaapi.repository.people.ClientRepository;

@RestController
@RequestMapping("/people/client")
public class ClientController {
    
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Client>> searchId(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(clientRepository.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Client>> list(){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(clientRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Client> save (@RequestBody Client client){
        return ResponseEntity.status(HttpStatus.CREATED).body(clientRepository.save(client));
    }

    @PutMapping
    public ResponseEntity<Client> alteration(@RequestBody Client client){
        if(clientRepository.findById(client.getId()).isPresent()) 
            return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(clientRepository.save(client));
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
    }

    @DeleteMapping
    public ResponseEntity<Void> delete (@RequestBody Client client){
        try {
            clientRepository.deleteById(client.getId());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        
    }
}
