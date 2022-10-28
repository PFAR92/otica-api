package com.otica.oticaapi.repository.people;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otica.oticaapi.model.people.Provider;

public interface ProviderRepository extends JpaRepository<Provider, Long>{
    
}
