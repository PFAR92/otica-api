package com.otica.oticaapi.repository.people;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otica.oticaapi.model.people.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    
}
