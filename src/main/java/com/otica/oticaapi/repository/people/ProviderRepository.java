package com.otica.oticaapi.repository.people;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otica.oticaapi.model.people.Provider;

import java.util.List;
import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, Long>{
    Optional<Provider> findByCnpj(String cnpj);
    boolean existsByCnpj(String cnpj);

    Optional<Provider> findByName(String name);
    List<Provider> findByNameContaining(String name);
}
