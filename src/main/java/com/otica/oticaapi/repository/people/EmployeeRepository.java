package com.otica.oticaapi.repository.people;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otica.oticaapi.model.people.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

    Optional<Employee> findByCpf(String cpf);
    boolean existsByCpf(String cpf);

    List<Employee> findByNameContaining(String name);
}
