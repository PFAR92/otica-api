package com.otica.oticaapi.service.people;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otica.oticaapi.exceptions.NotFoudException;
import com.otica.oticaapi.model.people.Client;
import com.otica.oticaapi.repository.people.ClientRepository;

@Service
public class ClientService{

    @Autowired
    private ClientRepository clientRepository;

    
    public Client searchId(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new NotFoudException("Esse cliente não existe"));
    }

    
    public List<Client> list() {
        return clientRepository.findAll();
    }

    
    public Client save(Client client) {        
        return clientRepository.save(client);
    }

    
    public ResponseEntity<Client> alteration(Client client) {
        if(!clientRepository.existsById(client.getId()))
            throw new NotFoudException("Esse cliente não existe!");
        else return ResponseEntity.status(HttpStatus.ACCEPTED).body(clientRepository.save(client));
    }

    
    public ResponseEntity<Client> delete(Client client) {

        if(!clientRepository.existsById(client.getId()))
            throw new NotFoudException("Esse cliente não existe!");
        clientRepository.deleteById(client.getId());
        return ResponseEntity.noContent().build();
        
    }
}
