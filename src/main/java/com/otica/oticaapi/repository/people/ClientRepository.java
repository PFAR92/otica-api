package com.otica.oticaapi.repository.people;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otica.oticaapi.model.people.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{
    List <Client> findByNameContaining(String name);
}
