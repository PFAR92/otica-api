package com.otica.oticaapi.service.people;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger();


    public Client searchId(Client client) {
        logger.info("Solicitando busca por ID, ID digitado: " + client.getId());
        if(!clientRepository.existsById(client.getId())){
            throw new NotFoudException("Esse cliente nao existe");
        } else {
            logger.info("Cliente encontrado");
            return clientRepository.findById(client.getId()).get();
        }
    }

    public Client searchCpf (Client client){
        logger.info("Solicitando busca por CPF do Cliente, CPF digitado: " + client.getCpf());
        if(!clientRepository.existsByCpf(client.getCpf())){
            throw new NotFoudException("Esse cliente nao existe");
        }else{
            logger.info("CLiente encontrado");
            return clientRepository.findByCpf(client.getCpf()).get();
        }
    }
    public List<Client> searchNames (Client client) {
        logger.info("Solicitando busca por nome do Cliente, busca digitada: " + client.getName());
        return clientRepository.findByNameContaining(client.getName());
    }

    public List<Client> list() {
        logger.info("Solicitou busca por todos os clientes");
        return clientRepository.findAll();
    }

    
    public Client save(Client client) {

        logger.info("Solicitou cadastrar novo cliente");
        if (clientRepository.existsByCpf(client.getCpf())) {
            throw new NotFoudException("Cpf ja cadastrado!");
        }else{
            logger.info("Cliente cadastrado com sucesso");
            return clientRepository.save(client);
        }
    }

    
    public ResponseEntity<Client> alteration(Client client) {

        logger.info("Solicitou alterar o cliente com o ID: " + client.getId());
        if(!clientRepository.existsById(client.getId())) {
            throw new NotFoudException("Esse cliente nao existe!");
        }else{
            logger.info("Cliente com o ID: " + client.getId() + " alterado com sucesso!");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(clientRepository.save(client));
        }
    }

    
    public void delete(Client client) {

        logger.info("solicitou excluir cliente com o ID: " + client.getId());
        if(!clientRepository.existsById(client.getId()))
            throw new NotFoudException("Esse cliente nao existe!");
        clientRepository.deleteById(client.getId());
        logger.info("cliente com ID:" + client.getId() + " excluido com sucesso");
        ResponseEntity.noContent().build();

    }
}
