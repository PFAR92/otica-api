package com.otica.oticaapi.service.people;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otica.oticaapi.service.exceptions.NotFoudException;
import com.otica.oticaapi.model.people.Client;
import com.otica.oticaapi.repository.people.ClientRepository;

@Service
public class ClientService{

    @Autowired
    private ClientRepository clientRepository;


    public Client searchId(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new NotFoudException("Esse cliente nao existe"));
    }

    public Client searchCpf (String cpf){
        return clientRepository.findByCpf(cpf).orElseThrow(() -> new NotFoudException("Cpf nao encontrado"));
    }

    public Client seachName (String name){
        return clientRepository.findByName(name).orElseThrow(() -> new NotFoudException("Esse cliente nao existe"));
    }

    public List<Client> searchNames (String name) {
        return clientRepository.findByNameContaining(name);
    }

    public List<Client> list() {
        return clientRepository.findAll();
    }

    
    public Client save(Client client) {
        if (clientRepository.existsByCpf(client.getCpf()))
            throw new NotFoudException("Cpf ja cadastrado!");
        else return clientRepository.save(client);
    }

    
    public ResponseEntity<Client> alteration(Client client) {
        if(!clientRepository.existsById(client.getId()))
            throw new NotFoudException("Esse cliente nao existe!");
        else return ResponseEntity.status(HttpStatus.ACCEPTED).body(clientRepository.save(client));
    }

    
    public void delete(Long id) {

        if(!clientRepository.existsById(id))
            throw new NotFoudException("Esse cliente nao existe!");
        clientRepository.deleteById(id);
        ResponseEntity.noContent().build();

    }
}
