package com.otica.oticaapi.service.people;

import java.util.List;
import com.otica.oticaapi.repository.address.AddressRepository;
import com.otica.oticaapi.service.address.AddressService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otica.oticaapi.service.exceptions.NotFoundException;
import com.otica.oticaapi.model.people.Client;
import com.otica.oticaapi.repository.people.ClientRepository;

@Service
@Log4j2
public class ClientService{

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressService addressService;



    public Client searchId(Client client) {
        log.info("Solicitando busca por ID, ID digitado: " + client.getId());
        if(!clientRepository.existsById(client.getId())){
            throw new NotFoundException("Esse cliente nao existe");
        } else {
            log.info("Cliente encontrado");
            return clientRepository.searchId(client.getId()).get();
        }
    }

    public Client searchCpf (Client client){
        log.info("Solicitando busca por CPF do Cliente, CPF digitado: " + client.getCpf());
        if(!clientRepository.existsCpfClient(client.getCpf()).equals(1)){
            throw new NotFoundException("Esse cliente nao existe");
        }else{
            log.info("CLiente encontrado");
            return clientRepository.searchCpf(client.getCpf()).get();
        }
    }
    public List<Client> searchNames (Client client) {
        log.info("Solicitando busca por nome do Cliente, busca digitada: " + client.getName());
        return clientRepository.pesquisaNome(client.getName());
    }

    public List<Client> list() {
        log.info("Solicitou busca por todos os clientes");
        return clientRepository.findAllClients();
    }

    
    public ResponseEntity <Client> save(Client client) {

        log.info("Solicitou cadastrar novo cliente");
        if (clientRepository.existsCpfClient(client.getCpf()).equals(1)) {
            throw new NotFoundException("Cpf ja cadastrado!");
        }else{
            String cep = client.getAddress().getCep();
            client.setAddress(addressService.addressCep(cep));
            if (!addressRepository.existsById(cep)){
                addressRepository.save(client.getAddress());
            }
            log.info("Cliente cadastrado com sucesso");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(clientRepository.save(client));
        }
    }

    
    public ResponseEntity<Client> alteration(Client client) {

        log.info("Solicitou alterar o cliente com o ID: " + client.getId());
        if(!clientRepository.existsById(client.getId())) {
            throw new NotFoundException("Esse cliente nao existe!");
        }
        Client clientAlteration = clientRepository.findById(client.getId()).get();
        if (clientRepository.existsCpfClient(client.getCpf()).equals(1) && !clientAlteration.getCpf().equals(client.getCpf())) {
            throw new NotFoundException("Cpf ja esta cadastrado em outro cliente ativo!");
        } else {
            client.setAddress(addressService.addressCep(client.getAddress().getCep()));
            if (!addressRepository.existsById(client.getAddress().getCep())){
                addressRepository.save(client.getAddress());
            }
            log.info("Cliente com o ID: " + client.getId() + " alterado com sucesso!");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(clientRepository.save(client));
        }
    }

    
    public void delete(Client client) {

        log.info("solicitou excluir cliente com o ID: " + client.getId());
        if(!clientRepository.existsById(client.getId()))
            throw new NotFoundException("Esse cliente nao existe!");
        clientRepository.deleteById(client.getId());
        log.info("cliente com ID:" + client.getId() + " excluido com sucesso");
        ResponseEntity.noContent().build();

    }
}
