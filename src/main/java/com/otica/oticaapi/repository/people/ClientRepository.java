package com.otica.oticaapi.repository.people;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otica.oticaapi.model.people.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{
    
}
