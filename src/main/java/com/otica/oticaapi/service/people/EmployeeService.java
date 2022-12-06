package com.otica.oticaapi.service.people;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otica.oticaapi.exceptions.NotFoudException;
import com.otica.oticaapi.model.people.Employee;
import com.otica.oticaapi.repository.people.EmployeeRepository;

@Service
public class EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    
    public Employee searchId(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new NotFoudException(
            "Esse Funcionário não existe!"));
    }

    
    public List<Employee> list() {
        return employeeRepository.findAll();
    }

    
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    
    public ResponseEntity<Employee> alteration(Employee employee) {
        if(!employeeRepository.existsById(employee.getId()))
            throw new NotFoudException("Esse funcionário não existe!");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(employeeRepository.save(employee));
        
    }

   
    public ResponseEntity<Employee> delete(Employee employee) {
        if(!employeeRepository.existsById(employee.getId()))
            throw new NotFoudException("Esse Funcionário não existe!");
        employeeRepository.deleteById(employee.getId());
        return ResponseEntity.noContent().build();
    }
    
}
