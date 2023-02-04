package com.otica.oticaapi.service.people;

import java.util.List;
import com.otica.oticaapi.repository.address.AddressRepository;
import com.otica.oticaapi.service.address.AddressService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ClientService{

    private ClientRepository clientRepository;
    private AddressService addressService;



    public Client searchId(Client client) {
        log.info("Solicitando busca por ID, ID digitado: " + client.getId());
        existsClient(client.getId());
        log.info("CLiente encontrado");
        return clientRepository.findById(client.getId()).get();
    }
    public Client searchCpf (Client client){
        log.info("Solicitando busca por CPF do Cliente, CPF digitado: " + client.getCpf());
        existsClient(client.getCpf());
        log.info("CLiente encontrado");
        return clientRepository.findByCpf(client.getCpf()).get();
    }
    public List<Client> searchNames (Client client) {
        log.info("Solicitando busca por nome do Cliente, busca digitada: " + client.getName());
        return clientRepository.findByNameContaining(client.getName());
    }
    public List<Client> list() {
        log.info("Solicitou busca por todos os clientes");
        return clientRepository.findAll();
    }

    public Client save(Client client) {

        log.info("Solicitou cadastrar novo cliente");
        clientCannotBeRegistered(client);

        String cep = client.getAddress().getCep();
        client.setAddress(addressService.addressCep(cep));

        addressService.thisAddressDoesNotExist(client.getAddress());

        log.info("Cliente cadastrado com sucesso");
        return clientRepository.save(client);
    }
    public Client alteration(Client client) {

        log.info("Solicitou alterar o cliente com o ID: " + client.getId());
        existsClient(client.getId());

        clientCannotBeRegistered(client);

        client.setAddress(addressService.addressCep(client.getAddress().getCep()));
        addressService.thisAddressDoesNotExist(client.getAddress());

        log.info("Cliente com o ID: " + client.getId() + " alterado com sucesso!");
        return clientRepository.save(client);
    }
    public void delete(Client client) {

        log.info("solicitou excluir cliente com o ID: " + client.getId());
        existsClient(client.getId());
        clientRepository.deleteById(client.getId());
        log.info("cliente com ID:" + client.getId() + " excluido com sucesso");
    }







    public void existsClient(Long id){
        if (!clientRepository.existsById(id)){
            throw new NotFoundException("Esse cliente nao existe");
        }
    }
    public void existsClient(String cpf){
        if (!clientRepository.existsByCpf(cpf)){
            throw new NotFoundException("Nao existe cliente com esse cpf");
        }
    }

    public void clientCannotBeRegistered(Client client){
        if (client.getId() == null){
            if (clientRepository.existsByCpf(client.getCpf())){
                throw new NotFoundException("Ja existe um cliente cadastrado com esse cpf");
            }
        } else {
            Client clientAlteration = clientRepository.findById(client.getId()).get();
            if (clientRepository.existsByCpf(client.getCpf()) && !client.getCpf().equals(clientAlteration.getCpf())){
                throw new NotFoundException("Ja existe outro cliente cadastrado com esse cpf");
            }
        }
    }
}
