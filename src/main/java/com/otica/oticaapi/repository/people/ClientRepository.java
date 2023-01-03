package com.otica.oticaapi.repository.people;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otica.oticaapi.model.people.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

    /*List<Client> findByNameContaining(String name);

    Optional<Client> findByCpf(String cpf);
    boolean existsByCpf(String cpf);*/

    @Query(nativeQuery = true, value = "select * from client")
    List<Client> findAllClients();

    @Query(nativeQuery = true, value = "select * from client where id = (:id)")
    Optional<Client> searchId(Long id);

    @Query(nativeQuery = true, value = "select * from client where cpf = (:cpf)")
    Optional<Client> searchCpf(String cpf);

    @Query(nativeQuery = true, value = "select count(*) > 0 from client where cpf  = (:cpf)")
    Integer existsCpfClient(String cpf);

    @Query(nativeQuery = true, value = "select * from client where name like (%:name%)")
    List<Client>pesquisaNome(String name);



}
