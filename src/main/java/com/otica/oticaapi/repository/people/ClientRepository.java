package com.otica.oticaapi.repository.people;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otica.oticaapi.model.people.Client;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

    List<Client> findByNameContaining(String name);
    Optional<Client> findByName(String name);

    Optional<Client> findByCpf(String cpf);
    boolean existsByCpf(String cpf);

}
